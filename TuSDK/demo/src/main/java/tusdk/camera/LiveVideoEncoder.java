/**
 * TuSDKVideoDemo
 * LiveVideoEncoder.java
 *
 * @author		Yanlin
 * @Date		Jul 15, 2016 2:50:46 PM
 * @Copright	(c) 2015 tusdk.com. All rights reserved.
 *
 */
package tusdk.camera;

import android.media.MediaCodec;
import android.media.MediaFormat;

import com.ksyun.media.streamer.framework.ImgBufFrame;
import com.ksyun.media.streamer.framework.SrcPin;

import org.lasque.tusdk.core.seles.video.SelesSurfaceTextureEncoder;

import java.nio.ByteBuffer;

/**
 * LiveVideoEncoder.java
 *
 * Surface to Surface 硬编
 *
 * @author Yanlin
 *
 */
public class LiveVideoEncoder extends SelesSurfaceTextureEncoder
{
	private H264VideoCodec h264VideoCodec;

	public LiveVideoEncoder() {
		h264VideoCodec = new H264VideoCodec();
	}

	public SrcPin<ImgBufFrame> getSrcPin() {
		return h264VideoCodec.getSrcPin();
	}

	public long getEncodedFrames() {
		if (h264VideoCodec == null)
			return 0;

		return h264VideoCodec.getEncodedFrames();
	}

	public void sendExtraData() {
		if (h264VideoCodec != null) {
			h264VideoCodec.sendExtraData();
		}
	}

	public void onVideoEncoderStarted(MediaFormat format) {
		h264VideoCodec.onEncoderStarted(format);
	}

	public void onVideoEncoderFrameDataAvailable(ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
		h264VideoCodec.onEncodedFrameDataAvailable(byteBuffer, bufferInfo);
	}
}