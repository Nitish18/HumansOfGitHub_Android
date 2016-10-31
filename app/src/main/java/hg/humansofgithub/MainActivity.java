package hg.humansofgithub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView Main_tv1;
    EditText Main_et1;
    Button Main_bt1;

    String ace = "https://api.github.com/users/";
    String finalstring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Main_tv1 = (TextView) findViewById(R.id.main_tv1);
        Main_et1 = (EditText) findViewById(R.id.main_et1);
        Main_bt1 = (Button) findViewById(R.id.main_bt1);

        Main_bt1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String final_query = Main_et1.getText().toString();
                finalstring = ace.concat(final_query);
                Bundle bundle = new Bundle();
                bundle.putString("url", finalstring);
                Intent myIntent = new Intent(MainActivity.this,Userprofile.class);
                myIntent.putExtras(bundle);
                startActivity(myIntent);

            }
        });


    }
}
