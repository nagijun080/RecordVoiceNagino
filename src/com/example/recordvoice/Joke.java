package com.example.recordvoice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//ネタばらし
public class Joke extends Activity {

	private Button btn;
	private ImageView iv;
	String path = Environment.getExternalStorageDirectory()+ "/RecordVoice/testImage.jpg";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke);
        
        //もう一度再生ボタン
        btn = (Button)this.findViewById(R.id.button2);
        btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				voice();
			}
        });
        
        //録音音声の再生
        voice();
        //画像の表示
        show();
	}
	Bitmap bitmap;
	//画像の表示
	private void show(){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, opt);
		Log.d("bitmapに画像があるかどうか", bitmap.toString());
		if (bitmap == null) {
			iv = (ImageView)findViewById(R.id.imageView1);
			iv.setImageResource(R.drawable.scarypicture);
			return;
		}
		//画像があったら実行
		//if (bitmap != null) {
			int scaleW = opt.outWidth / 300; // →2
			int scaleH = opt.outHeight / 200; // →2
			int sampleSize = Math.max(scaleW, scaleH); // →2
			opt.inSampleSize = sampleSize;
			
			//opt.inJustDecodeBounds = false;
			Bitmap bmp = BitmapFactory.decodeFile(path, opt);
			
			int w = bmp.getWidth(); // →400
			int h = bmp.getHeight(); // →300
			float scale = Math.min((float)300/w, (float)200/h); // →0.6666667
			Matrix matrix = new Matrix();
			matrix.postScale(scale, scale);
			
			bmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
			
			iv = (ImageView)findViewById(R.id.imageView1);
			iv.setImageBitmap(bmp);
//		} else {
//			
//		}
	}
	
	//録音再生
  	private void voice() {
  		//インスタンス生成
      	VoicePlayer vplayer = new VoicePlayer();
      	vplayer.play();
  	}
	
	//ごめんなさいボタンでSorryアクティビティへ
	public void sorryOnClick(View v){
		Intent intent = new Intent(this, Sorry.class);
		this.startActivity(intent);
	}
}
