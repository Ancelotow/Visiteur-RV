package fr.gsb.rv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import fr.gsb.rv.entites.Visiteur;
import fr.gsb.rv.technique.Session;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    EditText matricul ;
    EditText pass ;
    @SuppressLint("ResourceAsColor")
    public void seConnecter(View v){
        matricul = (EditText) findViewById(R.id.matricul);
        pass = (EditText) findViewById(R.id.pass);
        String leMatricule = matricul.getText().toString();
        String leMdp = pass.getText().toString();
        Visiteur visiteur = new Visiteur( leMatricule, leMdp, null, null);
        try {
            String matricule = URLEncoder.encode( visiteur.getMatricule(), "UTF-8" ); // Le matricule
            String mdp = URLEncoder.encode( visiteur.getMdp(), "UTF-8" ); // Le mot de passe
            String url = String.format( "http://192.168.43.114:5000/visiteurs/%s/%s", matricule, mdp); //URL HTTP
            Response.Listener<JSONObject> ecouteurReponse = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        Visiteur unVisiteur = new Visiteur();
                        unVisiteur.setMatricule(response.getString("vis_matricule"));
                        unVisiteur.setNom(response.getString("vis_nom"));
                        unVisiteur.setPrenom(response.getString("vis_prenom"));
                        Session.ouvrir(unVisiteur);
                        Intent intentionEnvoyer = new Intent(MainActivity.this, MenuRvActivity.class);
                        startActivity(intentionEnvoyer);
                    } catch (JSONException e) {
                        Log.e("APP-RV", "Erreur JSON : "+ e.getMessage());
                    }
                }
            }; //Reponse Listener
            Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    TextView tvInfo = (TextView) findViewById(R.id.info);
                    tvInfo.setText("Echec de la connexion. Recommencez...");
                    Log.e("APP-RV", "Erreur HTTP : "+ error.getMessage());
                }
            }; //Error Listener
            JsonObjectRequest requete = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    ecouteurReponse,
                    ecouteurErreur
            );
            RequestQueue fileRequetes = Volley.newRequestQueue(this);
            fileRequetes.add(requete);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void annuler(View view){
        EditText edLogin = (EditText) findViewById(R.id.matricul);
        EditText edMdp = (EditText) findViewById(R.id.pass);
        TextView tvInfo = (TextView) findViewById(R.id.info);
        tvInfo.setText("");
        edLogin.setText("");
        edMdp.setText("");
    }

}
