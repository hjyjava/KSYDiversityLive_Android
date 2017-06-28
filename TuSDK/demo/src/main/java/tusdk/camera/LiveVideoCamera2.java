/**
 * TuSDKVideo
 * LiveVideoCamera.java
 *
 * @author		Yanlin
 * @Date		10:41:43 AM
 * @Copright	(c) 2015 tusdk.com. All rights reserved.
 *
 */
package tusdk.camera;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.widget.RelativeLayout;

import com.ksyun.media.streamer.framework.ImgBufFrame;
import com.ksyun.media.streamer.framework.SrcPin;

import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import org.lasque.tusdk.core.encoder.video.TuSDKVideoEncoderSetting;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSDKLiveVideoCamera;
import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;

import java.nio.ByteBuffer;

/**
 * LiveVideoCamera.java
 *
 * @author Yanlin
 *
 */
@SuppressWarnings("deprecation")
public class LiveVideoCamera2 extends TuSDKLiveVideoCamera implements PreviewCallback
{
    private LiveVideoEncoder liveVideoEncoder;

    /**
     * @param context
     * @param holderView
     */
    public LiveVideoCamera2(Context context, RelativeLayout holderView)
    {
        super(context, holderView);

        liveVideoEncoder = new LiveVideoEncoder();
    }

    public void initParams(TuSDKVideoEncoderSetting config) {

        // 自定义编码器 (默认： AVCodecType.HW_CODEC)
        TuSDKVideoCaptureSetting captureSetting = new TuSDKVideoCaptureSetting();
        // 默认硬编
        captureSetting.videoAVCodecType = TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC;
//        captureSetting.videoSize = new TuSdkSize(240, 424);
        captureSetting.fps = 24;
//        captureSetting.imageFormatType = TuSDKVideoCaptureSetting.ImageFormatType.NV21;
        setVideoCaptureSetting(captureSetting);

        // 配置视频输出参数
        // SDK 会根据设置自动调整预览尺寸、帧率
        // 设置后请勿再更改
        this.setVideoEncoderSetting(config);

        initOutputSettings();

        this.setEnableAudioCapture(true);

        //
        // 设置为横屏
        // this.setOutputImageOrientation(InterfaceOrientation.PortraitUpsideDown);
        // 水平镜像前置摄像头
        this.setHorizontallyMirrorFrontFacingCamera(true);
        // 禁用前置摄像头自动水平镜像 (默认: false，前置摄像头拍摄结果自动进行水平镜像)
        this.setDisableMirrorFrontFacing(false);

        // 开启智能美颜和动态贴纸 (默认: false)
        this.setEnableLiveSticker(false);

        // 如果设置为自定义编码，则指定代理获取帧数据
        if (this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.CUSTOM_CODEC)
        {
            // 动态贴纸必须和硬编配合才能使用
            this.setEnableLiveSticker(false);
        }
        else
        {
            setVideoDataDelegate(mVideoDataDelegate);
        }
    }

    @Override
    protected void onCameraStarted()
    {
        super.onCameraStarted();

        // 开机启动美颜
        // switchFilter("VideoFair");
//        startRecording();
    }

    /**
     * 初始化相机
     */
    @Override
    protected void onInitConfig(Camera camera)
    {
        super.onInitConfig(camera);

        // Parameters mParams = camera.getParameters();
        // 这里配置相机参数

        // camera.setParameters(mParams);
    }

    /**
     * 视频编码器委托
     */
    private TuSDKVideoDataEncoderDelegate mVideoDataDelegate = new TuSDKVideoDataEncoderDelegate()
    {
        @Override
        public void onVideoEncoderStarted(MediaFormat format)
        {
            TLog.d("onVideoEncoderStarted == ");

            liveVideoEncoder.onVideoEncoderStarted(format);
        }

        @Override
        public void onVideoEncoderFrameDataAvailable(long tms, ByteBuffer byteBuffer, BufferInfo bufferInfo)
        {
            TLog.d("onVideoEncoderFrameDataAvailable");

            liveVideoEncoder.onVideoEncoderFrameDataAvailable(byteBuffer, bufferInfo);
        }

        @Override
        public void onVideoEncoderCodecConfig(long l, ByteBuffer byteBuffer, BufferInfo bufferInfo) {
            TLog.d("onVideoEncoderCodecConfig");

            liveVideoEncoder.onVideoEncoderFrameDataAvailable(byteBuffer, bufferInfo);
        }
    };

    public SrcPin<ImgBufFrame> getSrcPin() {
        if (liveVideoEncoder == null)
            return null;

        return liveVideoEncoder.getSrcPin();
    }

    public long getEncodedFrames() {
        if (liveVideoEncoder == null)
            return 0;

        return liveVideoEncoder.getEncodedFrames();
    }

    public void sendExtraData() {
        if (liveVideoEncoder != null) {
            liveVideoEncoder.sendExtraData();
        }
    }

}
