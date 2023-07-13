package com.example.anigram.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.anigram.ui.AnimeDetailActivity;
import com.example.anigram.ui.profileFragments.ListFragment;
import com.example.anigram.R;
import com.example.anigram.ui.RandomAnimeGeneratorActivity;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.Arrays;

import dev.katsute.mal4j.anime.AnimeListStatus;
import dev.katsute.mal4j.anime.property.AnimeStatus;

//Eine Klasse welche für die Funktion um ein Anime: hinzuzufügen, editieren, löschen -> verantwortlich ist.
//Diese selbsternannte Service Klasse sorgt für die Popups bzw. die untere Leiste wenn man ein Anime editieren will.
public class AnimeDialogService extends Application {

    //Deklarierung aller notwendigen Variablen
    private AlertDialog editAnimeDialog;
    private ListFragment listFragment;

    //Klassen von der man den Service aufrufen kann.
    private AnimeDetailActivity animeDetailActivity;
    private AnimeListStatus animeListStatus;

    //Utils
    private int currentPositionFromList = -1; //Hält fest in welcher Position man sich in der Listen-RecyclerView man sich befindet.
    private boolean saveButtonPressed = false; //Prüft ob der save Button schon gedrückt wurde -> um mehrere API Anfragen auszuschließen.

    //UI Stuffs
    private View outsideOfEdit;
    private PowerSpinnerView statusSpinner;
    private PowerSpinnerView ratingSpinner;
    private EditText editEpisodes;
    private ImageButton deleteButton;
    private ImageButton saveButton;
    private TextView decrementButton;
    private TextView incrementButton;
    private int episodesWatched;
    private View alertCustomDialog;

    private String[] ANIME_STATUS = new String[] {"Completed", "OnHold", "Watching", "Dropped", "PlanToWatch"};
    private String[] ANIME_UPDATE_STATUS_ENUMS = new String[] {"completed", "on_hold", "watching", "dropped", "plan_to_watch"};

    //Konstruktur welche die Hauptmethoden aufruft und den Context speichert.
    public AnimeDialogService(Context context) {
        initDialog(context); //Ruft die Methode auf -> Deklariert die UI Elemente und baut den Popup für zukünftige Aufrufe auf.
        initOnClicksForEdit(); //Deklariert schonmal die OnClickListener.
    }

    //Baut das Popup und setzt schonmal die UI Elemente -> einmaliges bauen -> mehrmaliges anzeigen
    private void initDialog(Context context) {
        //Setzt das Edit - Popup als View
        alertCustomDialog = LayoutInflater.from(context).inflate(R.layout.edit_anime_item, null);

        //Setzt UI Elemente
        outsideOfEdit = alertCustomDialog.findViewById(R.id.outside_of_edit);
        statusSpinner = alertCustomDialog.findViewById(R.id.spinner_status);
        ratingSpinner = alertCustomDialog.findViewById(R.id.spinner_rating);
        editEpisodes = alertCustomDialog.findViewById(R.id.edit_eps_watched);
        outsideOfEdit = alertCustomDialog.findViewById(R.id.outside_of_edit);
        decrementButton = alertCustomDialog.findViewById(R.id.edit_anime_decrement);
        saveButton = alertCustomDialog.findViewById(R.id.edit_save_anime);
        incrementButton = alertCustomDialog.findViewById(R.id.edit_anime_increment);
        deleteButton = alertCustomDialog.findViewById(R.id.edit_delete_anime);

        //Baut den Popup
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        alertDialog.setView(alertCustomDialog);

        editAnimeDialog = alertDialog.create();
        editAnimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editAnimeDialog.getWindow().getAttributes().windowAnimations = R.style.EditDialogTheme; //Zeigt das Popup mit einem fade und fade-out
    }

    //Methode um ein Anime in die eigene Animeliste (API und offline) hinzuzufügen.
    public void addAnime(Long animeId) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                GlobalVariables.mal.updateAnimeListing(animeId).status(AnimeStatus.PlanToWatch).update();
                statusSpinner.selectItemByIndex(4);
                GlobalVariables.animeList.add(GlobalVariables.mal.getAnime(animeId).getListStatus());
                toggleEditScreen();
            }
        });
    }

    //Setzt das derzeitige AnimeListStatus für den ganzen Popup -> Aus Liste (Profile)
    public void setAnimeListStatus(AnimeListStatus animeListStatus, int position, ListFragment listFragment) {
        this.animeListStatus = animeListStatus;
        this.currentPositionFromList = position;
        this.listFragment = listFragment;

        setValues();
    }

    //Setzt das derzeitige AnimeListStatus für den ganzen Popup -> Aus AnimeDetail
    public void setAnimeListStatus(AnimeListStatus animeListStatus, AnimeDetailActivity animeDetailActivity) {
        this.animeListStatus = animeListStatus;
        this.animeDetailActivity = animeDetailActivity;

        setValues();
    }

    //Setzt alle Werte in die Spinners und EditText
    private void setValues() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run () {
                if(animeListStatus.getStatus() != null) {
                    statusSpinner.selectItemByIndex(Arrays.asList(ANIME_STATUS).indexOf(animeListStatus.getStatus().toString()));
                    if(animeListStatus.getScore() > 0) ratingSpinner.selectItemByIndex(animeListStatus.getScore()-1);
                    if(animeListStatus.getWatchedEpisodes() >= 0) {
                        episodesWatched = animeListStatus.getWatchedEpisodes();
                        editEpisodes.setText(animeListStatus.getWatchedEpisodes().toString());
                    }
                }
            }
        });
    }

    //Togglet zwischen Popup -> Invisible und Visible
    public void toggleEditScreen() {
        try {
            if(editAnimeDialog.isShowing()) editAnimeDialog.cancel();
            else editAnimeDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Ansammlung aller OnClickListeners im PopUp
    private void initOnClicksForEdit() {

        //Wenn außerhalb des Popups geklickt wird -> Popup wird versteckt.
        outsideOfEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAnimeDialog.cancel();
            }
        });

        //Spinner für Status
        statusSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                //Wenn Status auf Completed gesetzt wird -> Setze die geschauten Episoden auf die EpisodenAnzahl des Animes.
                if(newIndex == 0) {
                    editEpisodes.setText(animeListStatus.getAnime().getEpisodes().toString());
                    episodesWatched = animeListStatus.getAnime().getEpisodes();
                }
            }
        });

        //Wenn eine Eingabe im EditText stattfindet.
        editEpisodes.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            //Wenn Enter gedrückt wird.
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    int eps = 0;
                    try {
                        eps = Integer.parseInt(v.getText().toString()); //Setzt die eps Variable als die Eingabe.
                    } catch(Exception e) { //Falls ein Fehler passiert ist den Text in ein Integer zu verwandeln.
                        Toast.makeText(editEpisodes.getContext(), "Please type in a number", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    //Falls die Eingabe kleiner als die maximale Episodenanzahl ist.
                    if(eps > 0 && eps < animeListStatus.getAnime().getEpisodes()) {
                        episodesWatched = Integer.parseInt(editEpisodes.getText().toString());
                    } else {
                        //Falls die Eingabe so groß ist wie die maximale Episodenanzahl -> Setze Status auf "completed"
                        episodesWatched = animeListStatus.getAnime().getEpisodes();
                        statusSpinner.selectItemByIndex(0);
                    }

                    //Versteck die Tastatur.
                    editEpisodes.clearFocus();
                    return true;
                }
                return false;
            }
        });

        //Wenn auf das Plus Zeichen gedrückt wird.
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(episodesWatched + 1 <= animeListStatus.getAnime().getEpisodes()) {
                    episodesWatched += 1;
                    editEpisodes.setText(String.valueOf(episodesWatched)); //Setze die inkrementierte Episodenanzahl in den EditText
                }
            }
        });

        //Wenn auf Minus Zeichen gedrückt wird.
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(episodesWatched - 1 >= 0) {
                    episodesWatched -= 1;
                    editEpisodes.setText(String.valueOf(episodesWatched)); //Setze die dekrementierte Episodenanzahl in den EditText
                }
            }
        });

        //Wenn aufs Speichern Button gedrückt wird.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                try {
                    //Falls nicht mehr als einmal auf save button geklickt wurde ->
                    if (!saveButtonPressed) {
                        saveButtonPressed = true;

                        //Aktualisiere mithilfe des API-Wrappers den Anime.
                        animeListStatus.edit()
                                .episodesWatched(Integer.parseInt(editEpisodes.getText().toString()))
                                .status(ANIME_UPDATE_STATUS_ENUMS[statusSpinner.getSelectedIndex()])
                                .score(ratingSpinner.getSelectedIndex() + 1)
                                .update();

                        //Falls das Dialog vom ListFragment geöffnet wurde -> rufe entsprechende Methoden auf um die Liste zu aktualisieren.
                        if (listFragment != null) {
                            //Ersetze den Anime in unserer offline Variable die alle Anime festhält.
                            GlobalVariables.animeList.set(GlobalVariables.animeList.indexOf(animeListStatus), GlobalVariables.mal.getAnime(animeListStatus.getAnime().getID()).getListStatus());
                            listFragment.filterThroughEverything();
                            toggleEditScreen();
                        } else {
                            toggleEditScreen();

                            //Schaue erstmal in unserer offline Animelist wie die Position des geklickten Animes ist.
                            int index = 0;
                            for (AnimeListStatus anime : GlobalVariables.animeList) {
                                if (anime.getAnime().getID().equals(animeListStatus.getAnime().getID())) {
                                    break;
                                }
                                index++;
                            }

                            //Ersetze den Anime in unserer offline Variable die alle Anime festhält.
                            GlobalVariables.animeList.set(index, GlobalVariables.mal.getAnime(animeListStatus.getAnime().getID()).getListStatus());
                        }
                    }
                    saveButtonPressed = false;
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Anime couldn't save, please check your entries", Toast.LENGTH_LONG).show();
                    saveButtonPressed = false;
                }
            }
        });

        //Wenn aufs Löschen Button gedrückt wird.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Rufe das "bist du dir sicher?" Popup (durch unsere Library) auf
                PopupDialog.getInstance(view.getContext())
                    .setStyle(Styles.STANDARD)
                    .setHeading("Delete Anime")
                    .setDescription("Are you sure you want to delete the anime?"+
                            " This action cannot be undone")
                    .setPopupDialogIcon(R.drawable.trash)
                    .setPositiveButtonBackground(R.color.red)
                    .setPopupDialogIconTint(R.color.red)
                    .showDialog(new OnDialogButtonClickListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onPositiveClicked(Dialog dialog) {
                            //Falls das Dialog vom ListFragment geöffnet wurde -> rufe entsprechende Methoden auf um die Liste zu aktualisieren.
                            if(listFragment != null) {
                                toggleEditScreen();
                                listFragment.deleteAnimeFromList(animeListStatus.getAnime().getID(), currentPositionFromList);
                            } else {
                                //Async Methode um ein reibungslosen löschen zu ermöglichen -> ui sieht nicht laggy aus
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run () {
                                        try {
                                            toggleEditScreen();
                                            animeDetailActivity.animeDeletedAction(animeListStatus.getAnime().getID()); //Lösche die Anime-Methode.
                                        } catch (Exception e) {
                                            PopupDialog.getInstance(view.getContext())
                                                    .setStyle(Styles.FAILED)
                                                    .setDismissButtonText("OK")
                                                    .setHeading(animeListStatus.getAnime().getTitle() + " couldn't got deleted, try again.")
                                                    .showDialog();
                                        }
                                    }
                                });
                            }
                            super.onPositiveClicked(dialog);
                        }

                        @Override
                        public void onNegativeClicked(Dialog dialog) {
                            super.onNegativeClicked(dialog);
                        }
                    });
                }
        });
    }
}
