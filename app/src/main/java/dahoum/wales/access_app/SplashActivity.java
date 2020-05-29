package dahoum.wales.access_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.imageView);
        Button getStarted = findViewById(R.id.get_started_button);
        getStarted.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        });
//        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
//        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
//        final int bottomStartMargin = params.bottomMargin;
//        final int bottomEndMargin = (int) px;
//        Animation anim = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
//                params.bottomMargin = bottomStartMargin + (int) ((bottomEndMargin - bottomStartMargin) * interpolatedTime);
//                logo.setLayoutParams(params);
//                findViewById(R.id.mainTv).setVisibility(View.VISIBLE);
//                findViewById(R.id.descTv).setVisibility(View.VISIBLE);
//                getStarted.setVisibility(View.VISIBLE);
//            }
//        };
//        anim.setDuration(500);
//        logo.startAnimation(anim);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
        final float startBias = params.verticalBias;
        final float endBias = 0.3f;
        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) logo.getLayoutParams();
                params.verticalBias = startBias + ((endBias - startBias) * interpolatedTime);
                logo.setLayoutParams(params);
            }
        };
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                findViewById(R.id.descTv).setVisibility(View.VISIBLE);
                getStarted.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setDuration(500);
        logo.startAnimation(anim);
    }
}
