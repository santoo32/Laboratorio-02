package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Button;

public class ConfiguracionActivity  extends AppCompatActivity {

    private CheckBoxPreference checkpref;
    private EditTextPreference textpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setear_preferencias);


        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new ConfiguracionFragment())
                .commit();
    }
    public static class ConfiguracionFragment extends PreferenceFragmentCompat {


        @Override
        public void onCreatePreferences(Bundle bundle, String s) {
            setPreferencesFromResource(R.xml.configuracion_ui, s);

            CheckBoxPreference checkpref = (CheckBoxPreference) findPreference("check_box_preference_1");
            EditTextPreference textpref = (EditTextPreference) findPreference("edit_text_preference_1");

        }

    }

    
}
