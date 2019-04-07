package fr.gsb.rv.technique;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import fr.gsb.rv.entites.Visiteur;

public class Session {

    private static Session session = null ;
    private Visiteur leVisiteur ;

    private Session(Visiteur leVisiteur){
        super();
        this.leVisiteur = leVisiteur ;
    }

    public static Session getSession(){
        return session ;
    }

    public Visiteur getLeVisiteur(){
        return this.leVisiteur ;
    }

    public static void ouvrir(Visiteur visiteur){
        if( session == null ){
            session = new Session(visiteur);
        }
    }

    public static String fermer(){
        session = null ;
        return "fermer" ;
    }

}
