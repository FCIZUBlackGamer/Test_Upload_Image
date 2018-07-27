package walid.dwidar.com.walid;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoGin extends AppCompatActivity {
    EditText User_name;
    EditText Password;
    TextView new_account;
    Button login_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lo_gin);
        User_name=(EditText)findViewById(R.id.user_name);
        Password=(EditText)findViewById(R.id.password);
        new_account=(TextView)findViewById(R.id.new_account);
        login_user=(Button)findViewById(R.id.login_user);

        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoGin.this,Rgisteration.class));
            }
        });

        login_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = User_name.getText().toString();
                final String password = Password.getText().toString();

                    RequestQueue queue = Volley.newRequestQueue(LoGin.this);
                    StringRequest request = new StringRequest(Request.Method.POST, "https://192.168.1.3/LoginUser.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject( response );


                            //if (response.equals("Welcome Home!")) {
                                Toast.makeText(LoGin.this, jsonObject.getString( "response" ), Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("ERRRRORRR",e.toString());
                            }
                            //  database.UpdateData("1", name, password, "user");
                               // startActivity(new Intent(LoGin.this, User_Profile.class));
//                            } else {
//                                Toast.makeText(LoGin.this, "username or password Wrong", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoGin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", name);

                            params.put("password", password);

                            return params;
                        }
                    };
                    queue.add(request);

            }
        });

    }
}
