package ImageProcessing;

import java.io.InputStream;
import java.io.OutputStream;
class Sync implements Runnable
{
    public Sync(InputStream inps, OutputStream oups) {
        inps_ = inps;
        oups_ = oups;
    }
    public void run() {
        try
        {
            final byte[] buffer = new byte[1024];
            for (int len = 0; (len = inps_.read(buffer)) != -1; )
            {
                oups_.write(buffer, 0, len);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private final OutputStream oups_;
    private final InputStream inps_;
}

