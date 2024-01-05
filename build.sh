#!/usr/bin/env bash
# Reset
Color_Off='\033[0m'       # Text Reset

readonly BCyan="\033[1;36m"        # Cyan
readonly IYellow="\033[0;93m"      # Yellow

########################################################
#
#  想打指定 Flavor 的包
#  执行  build.sh + flavor
#  命令行中如果没有输入 flavor 参数 默认打 ymm: flavor 的包
#  e.g 想打 ymm Flavor 的包 :
#
#  build.sh -f ymm
#
#  无参数时默认 打包 ${FLAVOR_LIST} 中配置的 渠道列表.
#
########################################################

START=$(date +%s)

# debugging shell script
#set -x

# show error logs
set -e


WORKSPACE=$(pwd)
OUTPUT="${WORKSPACE}"/build/apks
OUTPUT_APP="${WORKSPACE}"/build/apks/app

# shellcheck disable=SC2034
CURRENT_FLAVOR=$1

#FLAVOR_LIST="ymm dev"
FLAVOR_LIST="ymm"

# check if necessary tools exists
type zipalign >/dev/null 2>&1 || {
  echo >&2 "zipalign required but it's not installed.  Aborting."
  exit 1
}
type java >/dev/null 2>&1 || {
  echo >&2 "java required but it's not installed.  Aborting."
  exit 1
}

publish_def() {
     for f in ${FLAVOR_LIST}; do
       publish_by_flavor "${f}"
    done
}

publish_by_flavor() { # -f
  cd "${WORKSPACE}"
  echo
  echo -e "${IYellow}App: BUILD  APK BEGINS ... ${Color_Off}"
  echo

  voice_broadcast "正在构建漫记APP."

  flavor=$1

  for f in ${flavor}; do
    # shellcheck disable=SC2021
    ASSEMBLE_FLAVOR=$(echo "${f:0:1}" | tr '[a-z]' '[A-Z]')${f:1}
    echo -e "${IYellow}App: FLAVOR $ASSEMBLE_FLAVOR IS BUILDING NOW ... ... ...${Color_Off}"
    gradle :app:assemble"${ASSEMBLE_FLAVOR}"Release
#    cd "${WORKSPACE}/app/build/outputs/apk/${f}/release"
    cp "${WORKSPACE}"/app/build/outputs/apk/"${f}"/release/*release.apk "${OUTPUT_APP}"
  done

  cd "${OUTPUT_APP}"
  # shellcheck disable=SC2045
  # shellcheck disable=SC2006
  # shellcheck disable=SC2046
  # shellcheck disable=SC2001
  for i in `ls`; do mv -f "${i}" `echo "${i}" | sed 's/^app/App/'`; done

  # 在打包时如果源文件包含绝对路径，打包的文件重新解压也会包含有目录信息，所以如果不需要目录信息需要使用 -C 参数。
  # 注意：-C 与文件夹之间没有空格，文件夹与文件中间有空格)
  tar -zcvf "${WORKSPACE}"/build/apks.tar.gz -C "${WORKSPACE}"/build/apks/ . #注意后面是 . 表示当前目录

#  # python2.7
#  python build.py update2

  echo
  echo -e "${IYellow}App: BUILD  APK SUCCESSFULLY !!! ${Color_Off}"
  echo

  cd "${WORKSPACE}"
}

publish_flavor() {
  flavor=$1
  publish_by_flavor "${flavor}"
}

mission_prepare() {
  cd "${WORKSPACE}"
  echo
  echo -e "${IYellow}App: CLEAN ALL PROJECTS ... ... ...${Color_Off}"
  echo

  START=$(date +%s)

  voice_broadcast "正在清理上次构建缓存."

  gradle clean
  rm -rf "${WORKSPACE}"/build
  mkdir -p "${OUTPUT}"
  mkdir -p "${OUTPUT_APP}"

  echo
  echo -e "${IYellow}App: BUILD APK START ... ... ...${Color_Off}"
  echo
}

mission_completed() {
  echo
  echo -e "${IYellow}App: SUCCESS ... ... ...${Color_Off}"
  echo -e "${IYellow}App: ALL BUILDS GENERATED IN: $OUTPUT${Color_Off}"
  echo

  END=$(date +%s)
  # shellcheck disable=SC2004
  DIFF=$(($END - $START))

  echo
  echo "Build TIME IS: ${DIFF}s"
  echo

  voice_broadcast "漫记APP构建脚本执行完毕!"

  if [[ "$OSTYPE" == "darwin"* ]]; then
    open "${OUTPUT}"
  fi
}

voice_broadcast() {
  if [[ "$OSTYPE" == "darwin"* ]]; then
    message=$1
    say -v'Mei-Jia' "${message}"
  fi
}

publish_flavor() {
  flavor=$1
  publish_by_flavor "${flavor}"
}

app_distribution() {
#   签渠道
  cd "${OUTPUT_APP}"

}


deploy(){
  mission_prepare
  publish_def
  app_distribution
  mission_completed
}

################################################################################
# Help                                                                         #
################################################################################
Help() {
  # Display Help
  echo
  echo -e "${BCyan}###########################${Color_Off}"
  echo -e "${BCyan}###                     ###${Color_Off}"
  echo -e "${BCyan}###     予满满 SCRIPT    ###${Color_Off}"
  echo -e "${BCyan}###                     ###${Color_Off}"
  echo -e "${BCyan}###########################${Color_Off}"
  echo
  echo -e "${BCyan}Usage: flavor [option...][param...]${Color_Off}"
  echo
  echo -e "${BCyan}Options:${Color_Off}"
  echo -e "${BCyan}   -h   --help       -help       帮助文档.${Color_Off}"
  echo -e "${BCyan}   -v   --version    -version    版本号."
  echo -e "${BCyan}   -f   --flavor     -flavor     指定 flavor 构建 APK.${Color_Off}"
  echo -e "${BCyan}   -a   --all        -all        构建全部 flavor.${Color_Off}"
  echo -e "${BCyan}   -l   --list       -list       显示 flavor 列表.${Color_Off}"
  echo -e "${BCyan}   -d   --deploy     -deploy     构建全部 flavor 并发布.${Color_Off}"
  echo
}

Version() {
  echo
  #  1.0.3 新增 admob 分支用于测试 admob 广告.
  echo "Script Version: 1.0.3"
  echo
}

###############################################################################
# Get the script options
###############################################################################
while getopts "lhvf:ad-" OPTION; do
  # shellcheck disable=SC2213
  # shellcheck disable=SC1009
  # shellcheck disable=SC2214
  case "${OPTION}" in
  h) # display Help (-h)
    Help
    exit
    ;;
  v) # display script version code (-v)
    echo -e "${IYellow}$(Version)${Color_Off}"
    echo
    exit
    ;;
  l) # display available flavor list (-l)
    echo
    echo -e "${IYellow}App Flavors List:${Color_Off}"
    echo -e "${IYellow}      ymm      : 公版渠道${Color_Off}"
    echo
    exit
    ;;
  f) # building apks by specified flavors (-f)
    echo
    echo -e "${IYellow}App: Building Specified Flavor Apks From: $OPTARG${Color_Off}"
    echo
    mission_prepare
    publish_flavor "${OPTARG}"
    mission_completed
    exit
    ;;
  a) # building all flavors from ${FLAVOR_LIST} (-a)
    echo
    echo -e "${IYellow}App: Building All Flavor Apks: $FLAVOR_LIST${Color_Off}"
    echo
    mission_prepare
    publish_def
    mission_completed
    exit
    ;;
  d) # building all side apks and publish to deploy dir(-d)
    echo
    echo -e "${IYellow}App: Building and Distribute All Side Apks...${Color_Off}"
    echo
    mission_prepare
    publish_def
    app_distribution
    mission_completed
    exit
    ;;
  --)
    # shellcheck disable=SC2003
    [[ "${OPTION}" -ge 1 ]] && optind=$(expr "${OPTION}" - 1) || optind="${OPTION}"
    eval OPTION="\$$optind"
    OPTARG=$(echo "${OPTION}" | cut -d'=' -f2)
    OPTION=$(echo "${OPTION}" | cut -d'=' -f1)

    case "${OPTION}" in
    --help)
      Help
      exit
      ;;
    --version)
      Version
      exit
      ;;
    --list)
      echo
      echo -e "${IYellow}App Flavors List:${Color_Off}"
      echo -e "${IYellow}      ymm      : 公版渠道${Color_Off}"
      echo
      exit
      ;;
    --all)
      echo
      echo -e "${IYellow}App: Building All Flavor Apks: $FLAVOR_LIST${Color_Off}"
      echo
      mission_prepare
      publish_def
      mission_completed
      exit
      ;;
    --flavor)
      echo
      echo -e "${IYellow}App: Building Specified Flavor Apks From: $OPTARG${Color_Off}"
      echo
      mission_prepare
      publish_flavor "${OPTARG}"
      mission_completed
      exit
      ;;
    --deploy)
      echo -e "${IYellow}App: Building and Distribute All Side Apks...${Color_Off}"
      echo
      mission_prepare
      publish_def
      app_distribution
      mission_completed
      exit
      ;;
    *) _usage " Long: >>>>>>>> invalid options (long) " ;;
    esac
    OPTIND=1
    shift
    ;;
  \?) # incorrect option
    echo
    echo -e "${IYellow}App: Building ERROR: Invalid Option!!!${Color_Off}"
    voice_broadcast "参数错误,请查看帮助文档."
    echo
    exit
    ;;
  esac
done

# 没有参数时打印帮助文档以及脚本版本号.
#[[ $# = 0 ]] && Help && echo -e "${IYellow}$(Version)${Color_Off}"
deploy