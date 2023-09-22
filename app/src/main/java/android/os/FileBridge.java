package android.os;

import java.io.IOException;
import java.io.OutputStream;

public class FileBridge {

    public static class FileBridgeOutputStream extends OutputStream {
        public FileBridgeOutputStream(ParcelFileDescriptor clientPfd) {

        }

        @Override
        public void write(int i) throws IOException {

        }
    }

}
