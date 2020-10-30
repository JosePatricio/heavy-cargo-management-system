package ec.redcode.tas.on.android.activities.conductor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.PopupMenu;

import ec.redcode.tas.on.android.R;

public class EnrollTypeMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_type_main_activity);
/**
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(EnrollTypeMainActivity.this, getContentView());
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.menu_enrol, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.enrolIndependiente)
                    startActivity(new Intent(EnrollTypeMainActivity.this, ActivityEnrollmentIndependiente.class));
                else if (item.getItemId() == R.id.enrolCompTrans)
                    startActivity(new Intent(EnrollTypeMainActivity.this, ActivityEnrollment.class));

                return true;
            }
        });

        popup.show(); //showing popup menu
 **/
    }
}
