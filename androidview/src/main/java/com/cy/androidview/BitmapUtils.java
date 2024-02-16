package com.cy.androidview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lenovo on 2017/12/24.
 */

public class BitmapUtils {
    /**
     * 选择变换
     *
     * @param origin            原图
     * @param orientationDegree 旋转角度，可正可负
     * @return 旋转后的图片
     */
    public static Bitmap bitmapRotate(Bitmap origin, float orientationDegree) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(orientationDegree);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
    }

    /**
     * @param source
     * @param degree         比如90度，是顺时针旋转90度，-90度是逆时针旋转90度
     * @param flipHorizontal 是否左右镜像
     * @return
     */
    public static Bitmap bitmapRotate(Bitmap source, int degree, boolean flipHorizontal) {
        if (degree == 0 && !flipHorizontal) {
            return source;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        if (flipHorizontal)
            matrix.postScale(-1, 1);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }

    /**
     * 从底层返回的数据拿到对应的图片的角度，有些手机hal层会对手机拍出来的照片作相应的旋转，有些手机不会(比如三星手机)
     * //有些奇葩垃圾手机拍出来会被旋转，如小米奇葩垃圾手机 cc9,
     * //手机屏幕正上方指向正上方，图片逆时针旋转了90度，getHardwareOrientation 得到90度，
     * 手机屏幕正上方指向正左方，图片没有被旋转，getHardwareOrientation得到0度
     *
     * @return
     */
    public static int getPicDegree(InputStream inputStream) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(inputStream);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
//            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
//            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.log("getPicDegree", e.getMessage());
        }
        LogUtils.log("getPicDegree", degree);
        return degree;
    }

    /**
     * 从底层返回的数据拿到对应的图片的角度，有些手机hal层会对手机拍出来的照片作相应的旋转，有些手机不会(比如三星手机)
     * //有些奇葩垃圾手机拍出来会被旋转，如小米奇葩垃圾手机 cc9,
     * //手机屏幕正上方指向正上方，图片逆时针旋转了90度，getHardwareOrientation 得到90度，
     * 手机屏幕正上方指向正左方，图片没有被旋转，getHardwareOrientation得到0度
     *
     * @return
     */
//    public static int getHardwareOrientation(byte[] jpeg) {
//        if (jpeg == null) {
//            return 0;
//        }
//
//        int offset = 0;
//        int length = 0;
//
//        // ISO/IEC 10918-1:1993(E)
//        while (offset + 3 < jpeg.length && (jpeg[offset++] & 0xFF) == 0xFF) {
//            int marker = jpeg[offset] & 0xFF;
//
//            // Check if the marker is a padding.
//            if (marker == 0xFF) {
//                continue;
//            }
//            offset++;
//
//            // Check if the marker is SOI or TEM.
//            if (marker == 0xD8 || marker == 0x01) {
//                continue;
//            }
//            // Check if the marker is EOI or SOS.
//            if (marker == 0xD9 || marker == 0xDA) {
//                break;
//            }
//
//            // Get the length and check if it is reasonable.
//            length = pack(jpeg, offset, 2, false);
//            if (length < 2 || offset + length > jpeg.length) {
////                KSLog.e(TAG, "Invalid length");
//                return 0;
//            }
//
//            // Break if the marker is EXIF in APP1.
//            if (marker == 0xE1 && length >= 8 &&
//                    pack(jpeg, offset + 2, 4, false) == 0x45786966 &&
//                    pack(jpeg, offset + 6, 2, false) == 0) {
//                offset += 8;
//                length -= 8;
//                break;
//            }
//
//            // Skip other markers.
//            offset += length;
//            length = 0;
//        }
//
//        // JEITA CP-3451 Exif Version 2.2
//        if (length > 8) {
//            // Identify the byte order.
//            int tag = pack(jpeg, offset, 4, false);
//            if (tag != 0x49492A00 && tag != 0x4D4D002A) {
////                KSLog.e(TAG, "Invalid byte order");
//                return 0;
//            }
//            boolean littleEndian = (tag == 0x49492A00);
//
//            // Get the offset and check if it is reasonable.
//            int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
//            if (count < 10 || count > length) {
////                KSLog.e(TAG, "Invalid offset");
//                return 0;
//            }
//            offset += count;
//            length -= count;
//
//            // Get the count and go through all the elements.
//            count = pack(jpeg, offset - 2, 2, littleEndian);
//            while (count-- > 0 && length >= 12) {
//                // Get the tag and check if it is orientation.
//                tag = pack(jpeg, offset, 2, littleEndian);
//                if (tag == 0x0112) {
//                    // We do not really care about type and count, do we?
//                    int orientation = pack(jpeg, offset + 8, 2, littleEndian);
//                    switch (orientation) {
//                        case 1:
//                            return 0;
//                        case 3:
//                            return 180;
//                        case 6:
//                            return 90;
//                        case 8:
//                            return 270;
//                    }
////                    KSLog.i(TAG, "Unsupported orientation");
//                    return 0;
//                }
//                offset += 12;
//                length -= 12;
//            }
//        }
//        return 0;
//    }
//
//    private static int pack(byte[] bytes, int offset, int length,
//                            boolean littleEndian) {
//        int step = 1;
//        if (littleEndian) {
//            offset += length - 1;
//            step = -1;
//        }
//
//        int value = 0;
//        while (length-- > 0) {
//            value = (value << 8) | (bytes[offset] & 0xFF);
//            offset += step;
//        }
//        return value;
//    }
    public static String getSuffix(String filePath) {
        String suffix = "";
        try {
            suffix = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        } catch (Exception e) {

        }
        return suffix;
    }


    /**
     * @param bitmap
     * @return
     */
//    public static boolean saveBitmapToFile(Bitmap bitmap, File file) {
//        return saveBitmapToFile(bitmap, file, 100);
//    }

    /**
     * @param bitmap
     * @return
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, File file, int quality) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            if (file.getAbsolutePath().endsWith(".png")) {
                //PNG不支持压缩
                bitmap.compress(Bitmap.CompressFormat.PNG, quality, bos);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            }
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
//    public static boolean saveBitmapToPngFile(Bitmap bitmap, String path) {
//        return saveBitmapToPngFile(bitmap,createFile(path));
//    }
//    public static boolean saveBitmapToPngFile(Bitmap bitmap, File file) {
//        try {
//            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//            //写60页不会压缩，因为PNG不支持压缩
//            bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
//            bos.flush();
//            bos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }


    /**
     * 根据路径 创建文件
     *
     * @param pathFile
     * @return
     * @throws Exception 用Exception  防止其他异常
     */
    public static File createFile(String pathFile) {
        File file = null;
        try {
            File fileDir = new File(pathFile.substring(0, pathFile.lastIndexOf(File.separator)));
            file = new File(pathFile);
            if (!fileDir.exists()) fileDir.mkdirs();
            if (!file.exists()) file.createNewFile();
        } catch (Exception e) {

        }
        return file;
    }

//    /**
//     * 不压缩，传入压缩过的bitampa
//     *
//     * @param bitmap
//     * @return
//     */
//    public static boolean saveBitmapToFile(Bitmap bitmap, String path) {
//        File file = null;
//        try {
//            file = createFile(path);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return saveBitmapToFile(bitmap, file, 100);
//    }

    /**
     * 压缩，
     *
     * @param bitmap
     * @return
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String path, int quality) {
        return saveBitmapToFile(bitmap, createFile(path), quality);
    }

    public static boolean saveBitmapToFile_90(Bitmap bitmap, String path) {
        return saveBitmapToFile(bitmap, createFile(path), 90);
    }

    /**
     * 不压缩
     *
     * @return
     */
    public static boolean saveBytesToFile(byte[] bytes, String path) {
        File file = createFile(path);
        if (file == null) return false;

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
//    /*
//     * 由file转bitmap,压缩
//     */
//
//    public static Bitmap decodeBitmapFromStream(InputStream inputStream, long contentLength, int widthReq, int heightReq) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        // Calculate inSampleSize
//        //不让图片文件>500kb,不一定精准
//        float ratio = contentLength * 1f / 1024 / 500;
//        if (ratio >= 1) {
//            options.inSampleSize = (int) Math.round(ratio + 1.5);
//        } else {
//            options.inSampleSize = 2;
//        }
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
//        if (bitmap == null) return null;
//        return compressBitmap(bitmap, widthReq, heightReq);
//    }


    /*
     * 计算采样率
     */
//    public static int calculateInSampleSize(BitmapFactory.Options options,
//                                            int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            //使用需要的宽高的最大值来计算比率
            final int suitedValue = reqHeight > reqWidth ? reqHeight : reqWidth;
            final int heightRatio = Math.round((float) height / (float) suitedValue);
            final int widthRatio = Math.round((float) width / (float) suitedValue);

            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;//用最大
        }
        return inSampleSize;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidthxreqHeight) {
        return getInSampleSize(options.outWidth, options.outHeight, reqWidthxreqHeight);
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public static Bitmap cropBitmap(Bitmap bitmap, float hRatioW) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        return Bitmap.createBitmap(bitmap, 0, 0, w, (int) (w * hRatioW), null, false);
    }

    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
    }

    /*
     * 质量压缩法：将图片文件压缩,压缩是耗时操作
     */
    public static void compressBitmapToFile(CompressFileBean compressFileBean, CompressFileCallback compressFileCallback) {
        new CompressFileThread(compressFileBean, compressFileCallback).start();
    }

    /**
     * drawable raw目录均可以
     *直接使用BitmapFactory.decodeResource btimap 会被根据屏幕密度进行压缩，真是麻雀啄了牛屁股
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap decodeResourceOrigin(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        TypedValue value = new TypedValue();
        context.getResources().openRawResource(resId, value);
        options.inTargetDensity = value.density;
        options.inScaled = false;//不缩放
        return BitmapFactory.decodeResource(context.getResources(), resId, options);
    }

    /**
     * drawable raw目录均可以
     *
     * @param context
     * @return
     */
    public static Bitmap decodeResource(Context context, int id, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeResource(context.getResources(), id, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//如此，方可decode bitmap

        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }

//    public static Bitmap decodeBitmapFromRaw(Context context, @RawRes int id) {
//        return BitmapFactory.decodeStream(context.getResources().openRawResource(id));
//    }

    /**
     * drawable raw目录均可以
     *
     * @param context
     * @return
     */
    public static Bitmap decodeResource(Context context, int id, int reqWidthxreqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeResource(context.getResources(), id, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidthxreqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//如此，方可decode bitmap

        return BitmapFactory.decodeResource(context.getResources(), id, options);
    }

    public static Bitmap decodeByteArray(byte[] bytes, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//如此，方可decode bitmap

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }


    /*
     * 由file转bitmap
     */
    public static Bitmap decodeBitmapFromFilePath(String path, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//如此，方可decode bitmap

        return BitmapFactory.decodeFile(path, options);
    }

    /*
     * 由file转bitmap，3000*3000像素的bitmap argb_8888  占用内存34.3MB
     *                4000*4000像素的bitmap argb_8888  占用内存61MB
     *                5000*5000像素的bitmap argb_8888  占用内存95MB
     */
    public static Bitmap decodeBitmapFromFilePath3000(String path) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 9000000);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//如此，方可decode bitmap

        return BitmapFactory.decodeFile(path, options);
    }


    public static Bitmap decodeBitmapFromFilePathWxH(String path, int reqWidthxreqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidthxreqHeight);
        LogUtils.log("inSampleSize", options.inSampleSize);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//如此，方可decode bitmap

        return BitmapFactory.decodeFile(path, options);
    }


    public static int getInSampleSize(int width_pic, int height_pic, int reqWidthxreqHeight) {
        //最小为1,像素不能超过3000*3000,不能低于100*100,  insimplesize  >1则2,  >2则3
        return (int) Math.max(1, Math.ceil(Math.sqrt(width_pic * height_pic * 1f / reqWidthxreqHeight)));
    }

    /*
     * 由file转bitmap
     */
    public static Bitmap decodeBitmapFromFilePath(String path, int inSampleSize) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//如此，方可decode bitmap

        return BitmapFactory.decodeFile(path, options);
    }

    public static int[] getBitmapWHFromFilePath(String path) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//如此，无法decode bitmap
        BitmapFactory.decodeFile(path, options);
        return new int[]{options.outWidth, options.outHeight};
    }

    /**
     * 通过canvas复制view的bitmap
     *
     * @param view
     * @return
     */
    public static Bitmap getBitmapFromView(View view) {
        int width = view.getWidth();
        int height = view.getHeight();
        view.layout(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * Bitampa.Config_ARGB_8888,每个像素4个通道，每个通道8Bit,一个像素需要4个字节，
     * 5000x5000像素的图片占用内存=5000x5000x8x4/1024/1024=762.94MB
     *
     * @param bitmap
     * @param widthReq
     * @param heightReq
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int widthReq, int heightReq) {
        Matrix matrix = new Matrix();
        float ratio_width = widthReq * 1f / bitmap.getWidth();
        float ratio_height = heightReq * 1f / bitmap.getHeight();
        float ratio = Math.min(ratio_width, ratio_height);
        matrix.setScale(ratio, ratio);
        final Bitmap bitmap_result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap_result;
    }


    private static class CompressFileThread extends Thread {
        private Handler handler_deliver = new Handler(Looper.getMainLooper());
        private CompressFileBean compressFileBean;
        private CompressFileCallback compressFileCallback;
        private int quality = 100;


        public CompressFileThread(CompressFileBean compressFileBean, CompressFileCallback compressFileCallback) {
            this.compressFileBean = compressFileBean;
            this.quality = compressFileBean.getQuality_first();
            this.compressFileCallback = compressFileCallback;
        }

        @Override
        public void run() {
            super.run();
            final Bitmap bitmapOrigin = compressFileBean.getBitmapOrigin();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //quality，压缩精度尽量不要低于50，否则影响清晰度,不支持压缩PNG
            bitmapOrigin.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
//            long size=byteArrayOutputStream.size();
            while (byteArrayOutputStream.toByteArray().length / 1024 > compressFileBean.getKb_max() && quality > compressFileBean.getQuality_min()) {
//                LogUtils.log("compress", byteArrayOutputStream.toByteArray().length / 1024);
                // 循环判断如果压缩后图片是否大于kb_max kb,大于继续压缩,
                byteArrayOutputStream.reset();
                quality -= 10;
                bitmapOrigin.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
            }
            try {
                final File fileCompressed = createFile(compressFileBean.getPathCompressed());
                FileOutputStream fileOutputStream = new FileOutputStream(fileCompressed);
                fileOutputStream.write(byteArrayOutputStream.toByteArray());//写入目标文件
                fileOutputStream.flush();
                fileOutputStream.close();
                byteArrayOutputStream.close();
                if (fileCompressed != null && fileCompressed.length() > 0) {
                    final int[] wh = getBitmapWHFromFilePath(fileCompressed.getAbsolutePath());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //压缩成功
                            compressFileCallback.onCompressFileFinished(fileCompressed, wh[0], wh[1]);
                        }
                    });
                }

            } catch (final Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //压缩失败
                        compressFileCallback.onCompressFileFailed("压缩图片文件失败" + e.getMessage());
                    }
                });
            }

        }

        private void runOnUiThread(Runnable run) {
            handler_deliver.post(run);
        }

    }

    public static class CompressFileBean {
        //        private String pathSource;//原图文件路径
        private String pathCompressed;//压缩后的图片文件路径
        private int kb_max = 1;//压缩到多少KB，不能精确，只能<=kb_max
        private int quality_min = 10;//压缩精度，尽量>=50
        private int quality_first = 90;
        private Bitmap bitmapOrigin;

        public int getQuality_first() {
            return quality_first;
        }

        public CompressFileBean setQuality_first(int quality_first) {
            this.quality_first = quality_first;
            return this;
        }

        public String getPathCompressed() {
            return pathCompressed;
        }

        public CompressFileBean setPathCompressed(String pathCompressed) {
            this.pathCompressed = pathCompressed;
            return this;
        }

        public int getKb_max() {
            return kb_max;
        }

        public CompressFileBean setKb_max(int kb_max) {
            this.kb_max = kb_max;
            return this;

        }

        public int getQuality_min() {
            return quality_min;
        }

        public CompressFileBean setQuality_min(int quality_min) {
            this.quality_min = quality_min;
            return this;
        }

        public Bitmap getBitmapOrigin() {
            return bitmapOrigin;
        }

        public CompressFileBean setBitmapOrigin(Bitmap bitmapOrigin) {
            this.bitmapOrigin = bitmapOrigin;
            return this;
        }
    }

    public static interface CompressFileCallback {
        //图片压缩成功
        public void onCompressFileFinished(File file, int width, int height);

        //图片压缩失败
        public void onCompressFileFailed(String errorMsg);
    }

}
