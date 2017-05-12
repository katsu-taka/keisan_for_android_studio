package com.example.keisan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultActivity extends Activity {
	private TextView textansstatus, texthighscore;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		// TextViewStatus取得
		textansstatus = (TextView) findViewById(R.id.textViewAnserStatus);
		// TextViewStatus取得
		texthighscore = (TextView) findViewById(R.id.textHighScore);

		Intent intent = getIntent();
		String[] str = intent.getStringArrayExtra("mode");
		textansstatus.setText(str[1] + " / " + str[2] + "\n" + str[3]);
		// ハイスコア設定
		texthighscore.setText(getText(R.string.highScore) + str[4]);
	}

	public void clickReStart(View view) {
		// 「もう一度」ボタン押下後、ゲーム画面（RusltActivity -> TitleActivity）を開く
		Intent intent = new Intent();
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
