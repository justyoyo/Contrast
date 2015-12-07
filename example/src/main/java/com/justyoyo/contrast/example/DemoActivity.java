package com.justyoyo.contrast.example;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justyoyo.contrast.WriterException;
import com.justyoyo.contrast.qrcode.QRCodeEncoder;

public class DemoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ImageView mQRCodeImage;
        private TextView mQRCodeText;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_demo, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            mQRCodeImage = (ImageView) view.findViewById(R.id.qrcode_image);
            mQRCodeText = (TextView) view.findViewById(R.id.qrcode_text);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            float size = 192f * getResources().getDisplayMetrics().density;
            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder("4040111111111111", (int) size, null);
            Bitmap bitmap = null;
            try {
                bitmap = qrCodeEncoder.encodeAsBitmap();
            } catch (WriterException e) {
                e.printStackTrace();
            }
            finally {
                mQRCodeImage.setImageBitmap(bitmap);
                mQRCodeText.setText(String.format("Content: %s", qrCodeEncoder.getContents()));
            }
        }
    }

}
