package com.example.Ras;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hootsuite.nachos.ChipConfiguration;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.ChipSpan;
import com.hootsuite.nachos.chip.ChipSpanChipCreator;
import com.hootsuite.nachos.terminator.ChipTerminatorHandler;
import com.hootsuite.nachos.tokenizer.SpanChipTokenizer;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MissingActivity extends AppCompatActivity {

    private DatabaseReference dt_lessons;

    AutoCompleteTextView autoTextView;
    NachoTextView orderChips, diseaseChips, statementChips, validReasonChips;
    FloatingActionButton add_btn, sent_btn, cancel_btn;
    private boolean clicked = false;
    private String group;
    private List<String> dataChips = new ArrayList<>();
    private List<String> groupNumber = new ArrayList<>();
    Toolbar toolbar;

    private AccountHeader mHeader = null;
    private Drawer mDrawer = null;

    MainActivity mainActivity = new MainActivity();

    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;
    public static final String MISSING_PERSONS = "MISSING_PERSONS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing);
        orderChips = findViewById(R.id.chipTextOrder);
        diseaseChips = findViewById(R.id.chipTextDisease);
        statementChips = findViewById(R.id.chipTextStatement);
        validReasonChips = findViewById(R.id.chipTextValidReason);
        add_btn = findViewById(R.id.Add_FB);
        sent_btn = findViewById(R.id.Sent_FB);
        cancel_btn = findViewById(R.id.Upd_FB);
        autoTextView = findViewById(R.id.autoTextView);

        toolbar = findViewById(R.id.toolbarMissing);
        setSupportActionBar(toolbar);
        showHeader();
        showDrawer();

        dt_lessons = FirebaseDatabase.getInstance().getReference();

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);

        orderChips.setChipTokenizer(new SpanChipTokenizer<>(this, new ChipSpanChipCreator() {

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

        orderChips.addChipTerminator(' ', ChipTerminatorHandler.BEHAVIOR_CHIPIFY_TO_TERMINATOR);
        orderChips.enableEditChipOnTouch(false, false);

        Thread thread = new Thread(runnable);
        thread.start();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.group_number, groupNumber);
        adapter.notifyDataSetChanged();
        autoTextView.setAdapter(adapter);

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
                group = autoTextView.getText().toString();
                dataChips.addAll(orderChips.getChipValues());
                Log.d("MyLog", String.valueOf(dataChips.size()));
                Log.d("MyLog", group);
                for (int i = 0; i <= dataChips.size() - 1; i++) {
                    Log.d("MyLog", String.valueOf(dataChips.get(i)));
                }
                dataChips.clear();
            }
        });
    }

    public void showDrawer() {
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(-1)
                .withAccountHeader(mHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.timetable).withIcon(R.drawable.ic_schedule_24).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.messenger).withIcon(R.drawable.ic_message_24).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.missing).withIcon(R.drawable.ic_report_24).withIdentifier(3)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(@Nullable View view, int i, @NotNull IDrawerItem<?> iDrawerItem) {

                        switch (i) {
                            case 1:
                                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);;
                                startActivity(intent1);
                                break;
                            case 3:
                                Intent intent3 = new Intent(getApplicationContext(), MissingActivity.class);
                                intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent3);
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    public void showHeader() {
        mHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem().withName("Valentin")
                                .withEmail("123")
                                .withIcon(R.drawable.ic_school_24)
                )
                .build();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getDate(mainActivity.SetDate());
        }
    };

    private void getDate(String date) {
        Query query = dt_lessons.child(date);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Sender sender = dataSnapshot.getValue(Sender.class);
                    assert sender != null;
                    groupNumber.add(sender.numbGroup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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