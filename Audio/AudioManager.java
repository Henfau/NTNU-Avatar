package Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class AudioManager {

    private TargetDataLine targetline;
    private double[] frequencyData;

    public AudioManager() {

        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            TargetDataLine targetline = (TargetDataLine) AudioSystem.getLine(info);
            this.targetline = targetline;
            targetline.open(format,4096);
            System.out.println("Recording");
            targetline.start();

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private static int byteArrayToInt(byte[] b) {
        if (b.length == 4)
            return b[0] << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8
                    | (b[3] & 0xff);
        else if (b.length == 2)
            return 0x00 << 24 | 0x00 << 16 | (b[0] & 0xff) << 8 | (b[1] & 0xff);

        return 0;
    }

    public int[] readData(){
        while (true){
            /*
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            */


            int L = 4096;
            byte[] arr = new byte[L];
            int available;
            available=targetline.available();
            targetline.read(arr,0,L);

            //find biggest value
            long sum = 0;
            int[] values = new int[1024];
            for(int i = 0;i<L;i+=4) {
                byte[] word = new byte[4];
                word[0] = arr[i];
                word[1] = arr[i + 1];
                word[2] = arr[i + 2];
                word[3] = arr[i + 3];
                sum+= abs(byteArrayToInt(word));
                values[i/4] = byteArrayToInt(word);
            }
            return values;
        }

    }

    public void update(){
        this.frequencyData = FFT.handleAudioInput(readData());
    }
    public double[] getData(){
        return this.frequencyData;
    }
    public void close(){
        targetline.close();
    }

}