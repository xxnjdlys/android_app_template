#!/usr/bin/env bash

# 02:84:4E:79:0E:E5:74:49:AA:F2:F3:2A:34:6D:77:39:37:52:24:26:B6:B6:66:A6:2B:6D:A2:79:DA:07:A8:ED
# keytool -list -v -keystore ./mj.jks

# android debug fingurepring
# 1E:9A:0D:28:13:3E:E9:AA:EE:AD:94:2A:30:C1:93:24:BF:35:6B:7E:1B:AC:B1:DF:E7:5A:C1:75:F1:20:46:C6
# keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

# java -jar pepk.jar --keystore mj.jks --alias ymmbj --output=output.zip --encryptionkey=034200041E224EE22B45D19B23DB91BA9F52DE0A06513E03A5821409B34976FDEED6E0A47DBA48CC249DD93734A6C5D9A0F43461F9E140F278A5D2860846C2CF5D2C3C02 --include-cert

# keytool -export -rfc -keystore mj.jks -alias ymmbj -file upload_certificate.pem
