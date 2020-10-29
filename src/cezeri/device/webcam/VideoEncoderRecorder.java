/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeri.device.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * @author cezerilab
 */
public class VideoEncoderRecorder {

    public static void main(String[] args) throws Throwable {
        Dimension size = WebcamResolution.QVGA.getSize();
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(size);
        webcam.open(true);
        
        File file = new File("c:\\recorded_videos\\output.ts");
        IMediaWriter writer = ToolFactory.makeWriter(file.getAbsolutePath());
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);
        long start = System.currentTimeMillis();

        for (int i = 0; i < 50; i++) {

            System.out.println("Capture frame " + i);

            BufferedImage image = ConverterFactory.convertToType(webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
            IConverter converter = ConverterFactory.createConverter(image, IPixelFormat.Type.YUV420P);

            IVideoPicture frame = converter.toPicture(image, (System.currentTimeMillis() - start) * 1000);
            frame.setKeyFrame(i == 0);
            frame.setQuality(0);

            writer.encodeVideo(0, frame);

            // 10 FPS
            Thread.sleep(100);
        }

        writer.close();

        System.out.println("Video recorded in file: " + file.getAbsolutePath());
    }

}
