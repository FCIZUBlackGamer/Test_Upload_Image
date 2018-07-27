package walid.dwidar.com.walid;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Rgisteration extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgisteration);

        final FragmentManager manager=getSupportFragmentManager();
        User user=new User();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.frame,user);
        transaction.commit();

    }
}
