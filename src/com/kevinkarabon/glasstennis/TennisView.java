package com.kevinkarabon.glasstennis;

import java.lang.String;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TennisView extends FrameLayout {

	private final TextView mHomeScoreView;
	private final TextView mAwayScoreView;
	
	public TennisView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mHomeScoreView = (TextView) findViewById(R.id.homeScore);
		mAwayScoreView = (TextView) findViewById(R.id.awayScore);
	}
	
	public void updateView(String homeScore, String awayScore){
		mHomeScoreView.setText(homeScore);
		mAwayScoreView.setText(awayScore);
	}	
}