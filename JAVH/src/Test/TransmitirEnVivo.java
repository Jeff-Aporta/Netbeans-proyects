package Test;

import JAVH.FFmpeg;

public class TransmitirEnVivo {

    public static void main(String[] args) throws Exception {
        String pasword = "fvy5-eepb-b104-bvgx";
        String VideoTransmitir = "C:\\Users\\guillermo\\Documents\\Camtasia Studio\\Test noise\\Test noise.mp4";
        FFmpeg transmisor = new FFmpeg(VideoTransmitir);
        transmisor.HacerDirecto_hx264(FFmpeg.SERVER_STREAM_YOUTUBE, pasword);
    }
}
