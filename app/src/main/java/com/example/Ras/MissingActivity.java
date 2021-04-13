package com.example.Ras;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hootsuite.nachos.ChipConfiguration;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.ChipSpan;
import com.hootsuite.nachos.chip.ChipSpanChipCreator;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.tokenizer.SpanChipTokenizer;

public class MissingActivity extends AppCompatActivity {

    NachoTextView textChip;
    FloatingActionButton add_btn, sent_btn, cancel_btn;
    private boolean clicked = false;

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing);
        textChip = findViewById(R.id.chipText);
        add_btn = findViewById(R.id.Add_FB);
        sent_btn = findViewById(R.id.Sent_FB);
        cancel_btn = findViewById(R.id.Cancel_FB);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        textChip.setChipTokenizer(new SpanChipTokenizer<>(this, new ChipSpanChipCreator() {

            @Override
            public ChipSpan createChip(@NonNull Context context, @NonNull CharSequence text, Object data) {
                return new ChipSpan(context, text, ContextCompat.getDrawable(MissingActivity.this, R.drawable.ic_person_24), data);
            }

            @Override
            public void configureChip(@NonNull ChipSpan chip, @NonNull ChipConfiguration chipConfiguration) {
                super.configureChip(chip, chipConfiguration);
                chip.setShowIconOnLeft(true);
            }

        }, ChipSpan.class));

        textChip.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        textChip.enableEditChipOnTouch(false, false);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClicked();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String chip : textChip.getChipValues()) {

                    Log.d("MyLog", chip);
                }
            }
        });
    }

    private void onAddClicked() {
        setAnimation(clicked);
        setVisibility(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            cancel_btn.setVisibility(View.VISIBLE);
            sent_btn.setVisibility(View.VISIBLE);
        } else {
            cancel_btn.setVisibility(View.INVISIBLE);
            sent_btn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            sent_btn.startAnimation(fromBottom);
            cancel_btn.startAnimation(fromBottom);
            add_btn.startAnimation(rotateOpen);
        } else {
            sent_btn.startAnimation(toBottom);
            cancel_btn.startAnimation(toBottom);
            add_btn.startAnimation(rotateClose);
        }
    }

    private void setClickable(boolean clicked) {
        if (!clicked) {
            sent_btn.setClickable(true);
            cancel_btn.setClickable(true);
        } else {
            sent_btn.setClickable(false);
            cancel_btn.setClickable(false);
        }
    }
}