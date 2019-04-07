package fr.gsb.rv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.gsb.rv.technique.Session;

public class MenuRvActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConsult ;
    Button btnLogout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menurv);
        Session session = Session.getSession();
        TextView tvUser = (TextView) findViewById(R.id.user);
        String nom = session.getLeVisiteur().getNom();
        String prenom = session.getLeVisiteur().getPrenom();
        tvUser.setText(nom.toUpperCase() + " " + prenom );
        btnConsult = (Button) findViewById(R.id.btnConsult);
        btnConsult.setOnClickListener(this);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        Button btnSaisir = (Button) findViewById(R.id.btnSaisir);
        View.OnClickListener logout = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rep = Session.fermer();
                Intent intentionEnvoyer = new Intent(MenuRvActivity.this, MainActivity.class);
                startActivity(intentionEnvoyer);
            }
        };
        View.OnClickListener saisir = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentionEnvoyer = new Intent(MenuRvActivity.this, SaisirRvActivity.class);
                startActivity(intentionEnvoyer);
            }
        };
        btnLogout.setOnClickListener(logout);
        btnSaisir.setOnClickListener(saisir);
    }

    @Override
    public void onClick(View view) {
        Intent intentionEnvoyer = new Intent(this, ConsultRvActivity.class);
        startActivity(intentionEnvoyer);
    }
}
