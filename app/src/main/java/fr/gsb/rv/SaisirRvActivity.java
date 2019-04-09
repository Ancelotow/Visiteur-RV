package fr.gsb.rv;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import fr.gsb.rv.entites.Motif;
import fr.gsb.rv.entites.Praticien;
import fr.gsb.rv.entites.Visiteur;
import fr.gsb.rv.modeles.ModeleGsb;
import fr.gsb.rv.technique.Session;

public class SaisirRvActivity extends AppCompatActivity{

    protected List<Praticien> praticiens = new ArrayList<Praticien>() ;
    Spinner lvPraticiens ;
    protected List<Motif> motifs = new ArrayList<Motif>() ;
    Spinner lvMotif ;
    protected List<Integer> coefs = new ArrayList<Integer>();
    Spinner lvCoef;
    Button btnRetour;
    Button btnCancel;
    Button btnDate;
    Button btnValid;
    TextView tvDate;
    DatePickerDialog dialogDate;
    EditText edBilan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisirrv);
        lvPraticiens = (Spinner) findViewById( R.id.lvPraticiens) ;
        lvMotif = (Spinner) findViewById( R.id.lvMotifs) ;
        lvCoef = (Spinner) findViewById( R.id.lvConf) ;
        tvDate = (TextView) findViewById(R.id.tvDate);
        btnRetour = (Button) findViewById(R.id.btnRetour);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnValid = (Button) findViewById(R.id.btnValid);
        btnDate = (Button) findViewById(R.id.btnDate);
        edBilan = (EditText) findViewById(R.id.edBilan);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c;
                int mYear = 0;
                int mMonth = 0;
                int mDay = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR); // current year
                    mMonth = c.get(Calendar.MONTH); // current month
                    mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                }
                dialogDate = new DatePickerDialog(SaisirRvActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                tvDate.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dialogDate.show();
            }
        });
        View.OnClickListener quitter = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentionEnvoyer = new Intent(SaisirRvActivity.this,
                                                    MenuRvActivity.class);
                startActivity(intentionEnvoyer);
            }
        };
        View.OnClickListener annuler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edBilan.setText("");
                tvDate.setText("");
                Toast.makeText(SaisirRvActivity.this,
                                "Ce rapport de visite n'as pas été enregistré.",
                                Toast.LENGTH_LONG).show();
            }
        };
        View.OnClickListener ajouter = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRv();
                edBilan.setText("");
                tvDate.setText("");
            }
        };
        btnRetour.setOnClickListener(quitter);
        btnCancel.setOnClickListener(annuler);
        btnValid.setOnClickListener(ajouter);
        showPraticiens();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showMotifs();
        int coef = 1;
        while(coef <= 5){
            coefs.add(coef);
            coef = coef + 1;
        }
        ArrayAdapter<Integer> aaCoef = new ArrayAdapter<Integer>(this,
                                    android.R.layout.simple_spinner_item, coefs );
        lvCoef.setAdapter(aaCoef);
    }

    protected void addRv(){
        Praticien praticien = (Praticien) lvPraticiens.getSelectedItem();
        Motif motif = (Motif) lvMotif.getSelectedItem();
        String vis_matricule = Session.getSession().getLeVisiteur().getMatricule();
        String rv_date_visite = tvDate.getText().toString();
        int pra_num = praticien.getNum();
        int mo_code = motif.getCode();
        String rv_bilan = edBilan.getText().toString();
        int rv_confiance = (int) lvCoef.getSelectedItem();
        JSONObject json = new JSONObject();
        try {
            json.put("matricule", vis_matricule);
            json.put("praticien", pra_num);
            json.put("visite", rv_date_visite);
            json.put("bilan", rv_bilan);
            json.put("motif", mo_code);
            json.put("confiance", rv_confiance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = ModeleGsb.URL+"rapports"; //URL HTTP
        Response.Listener<JSONObject> ecouteurReponse = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("RV","Rép HTTP") ;
                Log.i("RV","Rép HTTP : " + response.length()) ;;
                Toast.makeText(SaisirRvActivity.this,
                        "Rapport de visite enregistrer avec succès", Toast.LENGTH_LONG).show();
            }
        }; //Reponse Listener
        Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SaisirRvActivity.this,
                        "Une erreur est survenu lors de l'enregistrement du rapport de visite",
                        Toast.LENGTH_LONG).show();
                Log.e("APP-RV", "Erreur HTTP : "+ error.getMessage());
            }
        }; //Error Listener
        JsonObjectRequest requete = new JsonObjectRequest(
                Request.Method.POST,
                url,
                json,
                ecouteurReponse,
                ecouteurErreur
        );
        RequestQueue fileRequetes = Volley.newRequestQueue(this);
        fileRequetes.add(requete);
    }

    protected void showPraticiens(){
            String url = ModeleGsb.URL+"praticiens"; //URL HTTP
            Response.Listener<JSONArray> ecouteurReponse = new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.i("RV","Rép HTTP") ;
                    Log.i("RV","Rép HTTP : " + response.length()) ;
                    try{
                        for(int i=0 ; i < response.length() ; i++){
                            Praticien unPraticien = new Praticien();
                            unPraticien.setNum(response.getJSONObject(i).getInt("pra_num"));
                            unPraticien.setNom(response.getJSONObject(i).getString("pra_nom"));
                            unPraticien.setPrenom(response.getJSONObject(i).getString("pra_prenom"));
                            SaisirRvActivity.this.praticiens.add(unPraticien);
                        }
                        System.out.println(praticiens.size());
                        ItemPraticienAdaptateur item = new ItemPraticienAdaptateur();
                        Log.i("RV","Rép HTTP - item " + item) ;
                        lvPraticiens.setAdapter(item);
                    } catch (JSONException e) {
                        Log.e("APP-RV", "Erreur JSON : "+ e.getMessage());
                    }
                }
            }; //Reponse Listener
            Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("APP-RV", "Erreur HTTP : "+ error.getMessage());
                }
            }; //Error Listener
            JsonArrayRequest requete = new JsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    ecouteurReponse,
                    ecouteurErreur
            );
            RequestQueue fileRequetes = Volley.newRequestQueue(this);
            fileRequetes.add(requete);
    }


    class ItemPraticienAdaptateur extends ArrayAdapter<Praticien>{

        ItemPraticienAdaptateur(){
            super(
                    SaisirRvActivity.this,
                    R.layout.item_praticien,
                    R.id.identite,
                    praticiens
            );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View vItem = super.getView(position, convertView, parent);
            TextView tvIdentite = (TextView) vItem.findViewById(R.id.num);
            tvIdentite.setText(" " + praticiens.get(position).getPrenom());
            return vItem;
        }

    }


    protected void showMotifs(){
        String url = ModeleGsb.URL+"motifs"; //URL HTTP
        Response.Listener<JSONArray> ecouteurReponse = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("RV","Rép HTTP") ;
                Log.i("RV","Rép HTTP : " + response.length()) ;
                try{
                    for(int i=0 ; i < response.length() ; i++){
                        Motif unMotif = new Motif();
                        unMotif.setCode(response.getJSONObject(i).getInt("mo_code"));
                        unMotif.setLibelle(response.getJSONObject(i).getString("mo_libelle"));
                        SaisirRvActivity.this.motifs.add(unMotif);
                    }
                    System.out.println(motifs.size());
                    ItemMotifAdaptateur itemMotif = new ItemMotifAdaptateur();
                    Log.i("RV","Rép HTTP - item " + itemMotif) ;
                    lvMotif.setAdapter(itemMotif);
                } catch (JSONException e) {
                    Log.e("APP-RV", "Erreur JSON : "+ e.getMessage());
                }
            }
        }; //Reponse Listener
        Response.ErrorListener ecouteurErreur = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("APP-RV", "Erreur HTTP : "+ error.getMessage());
            }
        }; //Error Listener
        JsonArrayRequest requete = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                ecouteurReponse,
                ecouteurErreur
        );
        RequestQueue fileRequetes = Volley.newRequestQueue(this);
        fileRequetes.add(requete);
    }


    class ItemMotifAdaptateur extends ArrayAdapter<Motif>{

        ItemMotifAdaptateur(){
            super(
                    SaisirRvActivity.this,
                    R.layout.item_motif,
                    R.id.motif,
                    motifs
            );
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View vItem = super.getView(position, convertView, parent);
            TextView tvMotif = (TextView) vItem.findViewById(R.id.motif);
            tvMotif.setText(motifs.get(position).getLibelle());
            return vItem;
        }

    }

}


