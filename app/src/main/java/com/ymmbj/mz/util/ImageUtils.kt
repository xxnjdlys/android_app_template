package com.ymmbj.mz.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.CancellationSignal
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Size
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ymmbj.mz.ktx.myApplication
import java.io.*

object ImageUtils {

    fun setImageUrl(imageView: ImageView, url: String?) {
        if (!TextUtils.isEmpty(url)) Glide.with(myApplication).load(url).into(imageView)
    }

    fun setImageUrl(imageView: ImageView, uri: Uri?) {
        uri?.let {
            Glide.with(myApplication).load(it).into(imageView)
        }
    }

    fun setImageUrl(imageView: ImageView, uri: Uri?, placeholder: Int) {
        uri?.let {
            Glide.with(myApplication).load(it).placeholder(placeholder).into(imageView)
        }
    }

    fun setImageUrl(imageView: ImageView, uri: String?, placeholder: Int) {
        uri?.let {
            Glide.with(myApplication).load(it).placeholder(placeholder).into(imageView)
        }
    }

    fun setImageUrl(imageView: ImageView, uri: Drawable?) {
        uri?.let {
            Glide.with(myApplication).load(it).into(imageView)
        }
    }

    fun setGifImageUrl(imageView: ImageView, url: String?, placeholder: Int) {
        url?.let {
            Glide.with(myApplication).asGif().load(it).placeholder(placeholder).into(imageView)
        }
    }

    fun setViewBackground(v: View, url: String, placeholder: Int) {
        Glide.with(v.context).asDrawable().placeholder(placeholder).load(url)
            .into(object : CustomTarget<Drawable?>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                    v.background = (resource)
                }
            })
    }

    fun setViewBackground(v: View, url: String) {
        Glide.with(v.context).asDrawable().load(url).into(object : CustomTarget<Drawable?>() {
            override fun onLoadCleared(placeholder: Drawable?) {}
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable?>?) {
                v.background = (resource)
            }
        })
    }

    fun getVideoThumbnail(ctx: Context?, path: String?): File? {
        if (path == null || TextUtils.isEmpty(path) || ctx == null) {
            return null
        }
        val videoThumbnailPath = ctx.cacheDir.path + "/VideoThumbnail"
        val pathFile = File(path)
        val saveFileName = if (!pathFile.exists()) {
            "${path.hashCode()}.png"
        } else {
            pathFile.name
        }
        val captureFile = File("$videoThumbnailPath/$saveFileName")
        if (captureFile.exists()) {
            return captureFile
        }
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT >= 29) {
            val mSize = Size(320, 320)
            try {
                bitmap = ThumbnailUtils.createVideoThumbnail(File(path), mSize, CancellationSignal())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND)
        }
        if (bitmap == null) {
            return null
        }
        val dirFile = File(videoThumbnailPath)
        if (!dirFile.exists()) {
            dirFile.mkdir()
        }
        var bos: BufferedOutputStream? = null
        try {
            bos = BufferedOutputStream(FileOutputStream(captureFile))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)
            return captureFile
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos?.flush()
            } catch (_: Exception) {
            }
            try {
                bos?.close()
            } catch (_: Exception) {
            }
        }
        return null
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getDrawableCompat(context: Context?, id: Int): Drawable? {
        context?.let {
            return if (Build.VERSION.SDK_INT >= 22) {
                it.resources.getDrawable(id, it.theme)
            } else {
                it.resources.getDrawable(id)
            }
        }
        return null
    }

    fun saveToImgByBytes(imageByte: ByteArray, dirFile: File): String {
        var fileAbsolutePath = ""
        if (imageByte.isNotEmpty()) {
            if (!dirFile.exists()) {
                dirFile.mkdir()
            }
            val file = File(dirFile, System.currentTimeMillis().toString() + ".jpg")
            fileAbsolutePath = file.absolutePath
            try {
                val in2: InputStream = ByteArrayInputStream(imageByte)
                val fos2 = FileOutputStream(file)
                val bytes = ByteArray(2048)
                while (true) {
                    val nRead: Int = in2.read(bytes)
                    if (nRead == -1) {
                        break
                    }
                    fos2.write(bytes, 0, nRead)
                }
                fos2.flush()
                try {
                    fos2.close()
                } catch (_: Exception) {
                }
                try {
                    in2.close()
                } catch (_: Exception) {
                }
            } catch (e5: IOException) {
                return fileAbsolutePath
            }
        }
        return fileAbsolutePath
    }

}