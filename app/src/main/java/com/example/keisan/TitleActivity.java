package com.example.keisan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TitleActivity extends Activity {
	private TextView texthighscore;
	// [0]:ゲームモード,[1]:正解数, [2]:出力数, [3]:回答率, [4]:ハイスコア値
	String[] ary = new String[5];

	private final String HIGH_SCORE = "highscore";
	private final int REQUEST_MAIN_CODE = 10;
	private final int REQUEST_RESULT_CODE = 20;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);

		// ハイスコアを取得
		int highsc = getHighScore();
		ary[4] = String.valueOf(highsc);
		// TextHighScore取得
		texthighscore = (TextView) findViewById(R.id.textHighScore);
		// ハイスコア設定
		texthighscore.setText(getString(R.string.highScore)
				+ String.valueOf(highsc));
	}

	// プリファレンスからハイスコアの初期値を取得
	private int getHighScore() {
		SharedPreferences prefs = getSharedPreferences("score",
				Context.MODE_PRIVATE);
		int highscore = prefs.getInt(HIGH_SCORE, 0);
		// Intent intent = getIntent();
		// String[] str = intent.getStringArrayExtra("mode");

		if (ary[1] != null) {
			if (Integer.parseInt(ary[1]) > highscore) {
				highscore = Integer.parseInt(ary[1]);
			}
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt(HIGH_SCORE, highscore);
			editor.apply();
		}

		return highscore;
	}

	public void clickStart(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		switch (view.getId()) {
		// 通常モードでゲーム画面（MainActivity）を開く
		case R.id.button1:
			ary[0] = "normal";
			intent.putExtra("mode", ary);
			startActivityForResult(intent, REQUEST_MAIN_CODE);
			break;
		// エンドレスモードでゲーム画面（MainActivity）を開く
		case R.id.button2:
			ary[0] = "endless";
			intent.putExtra("mode", ary);
			startActivityForResult(intent, REQUEST_MAIN_CODE);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// ゲーム画面（MainActivity）からの戻り
		if (requestCode == REQUEST_MAIN_CODE) {
			if (resultCode == RESULT_OK) {
				// String[] str = intent.getStringArrayExtra("mode");
				ary = intent.getStringArrayExtra("mode");
				switch (ary[0]) {
				case "normal":
					// ハイスコアを取得
					int highsc = getHighScore();
					ary[4] = String.valueOf(highsc);
					Intent nintent = new Intent(this, ResultActivity.class);
					nintent.putExtra("mode", ary);
					startActivityForResult(nintent, REQUEST_RESULT_CODE);
					break;

				case "endless":
					Intent eintent = new Intent(this, MainActivity.class);
					ary[0] = "endless";
					eintent.putExtra("mode", ary);
					startActivityForResult(eintent, REQUEST_MAIN_CODE);
					break;
				}
			}
		}

		// 結果画面（ResultActivity）からの戻り
		if (requestCode == REQUEST_RESULT_CODE) {
			if (resultCode == RESULT_OK) {
				Intent nintent = new Intent(this, MainActivity.class);
				ary[0] = "normal";
				nintent.putExtra("mode", ary);
				startActivityForResult(nintent, REQUEST_MAIN_CODE);
			}
		}
	}
}
