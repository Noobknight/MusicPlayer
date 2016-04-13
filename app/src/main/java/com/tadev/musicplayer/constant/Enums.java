package com.tadev.musicplayer.constant;

/**
 * Created by Iris Louis on 12/04/2016.
 */
public class Enums {
    public static enum Bitrate {
        MUSIC128(" [MP3 128Kbps]", 0),
        MUSIC320(" [MP3 320Kbps]", 1),
        MUSICM4A("-[M4A 500Kbps]", 2),
        MUSICLOSSLESS("-[LOSSLESS]", 3);

        private String stringValue;
        private int intValue;

        Bitrate(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        public static Bitrate valueOf(int value) {
            switch (value) {
                case 0:
                    return MUSIC128;
                case 1:
                    return MUSIC320;
                case 2:
                    return MUSICM4A;
                case 3:
                    return MUSICLOSSLESS;
                default:
                    return MUSIC128;
            }
        }

        public int value() {
            return intValue;
        }

        @Override
        public String toString() {
            return stringValue;
        }

    }

    public static enum PlayMode {
        LOOP(0),
        SHUFFLE(1),
        ONE(2);

        private int value;

        PlayMode(int value) {
            this.value = value;
        }

        public static PlayMode valueOf(int value) {
            switch (value) {
                case 0:
                    return LOOP;
                case 1:
                    return SHUFFLE;
                case 2:
                    return ONE;
                default:
                    return LOOP;
            }
        }

        public int value() {
            return value;
        }
    }
}
