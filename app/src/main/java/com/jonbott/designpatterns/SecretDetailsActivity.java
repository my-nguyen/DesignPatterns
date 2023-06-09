package com.jonbott.designpatterns;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;

public class SecretDetailsActivity extends AppCompatActivity {

    private Realm realm = Realm.getDefaultInstance();

    private int spyId = -1;
    private Spy spy;

    ProgressBar progressBar;
    TextView crackingLabel;
    Button finishedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_details);

        setupUI();
        parseBundle();
        crackPassword();
    }

    //region Helper Methods

    private void setupUI() {
        progressBar    = (ProgressBar) findViewById(R.id.secret_progress_bar);
        crackingLabel  = (TextView)    findViewById(R.id.secret_cracking_label);
        finishedButton = (Button)      findViewById(R.id.secret_finished_button);

        finishedButton.setOnClickListener(v -> finishedClicked() );

    }

    private void crackPassword() {
        Threading.async(()-> {
            //fake processing work
            Thread.sleep(2000);
            return true;
        }, success -> {
            progressBar.setVisibility(View.GONE);
            crackingLabel.setText(spy.password);
        });
    }

    private void parseBundle() {
        Bundle b = getIntent().getExtras();

        if(b != null)
            spyId = b.getInt(Constants.spyIdKey);

        spy = getSpy(spyId);
    }

    //endregion

    //region User Interaction

    private void finishedClicked() {
        Intent intent = new Intent(this, SpyListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //endregion

    //region Data loading
    private Spy getSpy(int id) {
        Spy tempSpy = realm.where(Spy.class).equalTo("id", id).findFirst();
        return realm.copyFromRealm(tempSpy);
    }
    //endregion


}
