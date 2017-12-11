package com.ait.kim.pantryrescue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ait.kim.pantryrescue.SearchData.SearchResult;
import com.ait.kim.pantryrescue.network.RecipeApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kimpham on 12/10/17.
 */

public class RecipeNamesActivity extends AppCompatActivity {

    private String itemName;
    public static final String RECIPE_ID = "RECIPE_ID";
    public String apiid = "7f1b23084710b3c5524faf0d773ee434";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_names_activity);

        final TextView error = new TextView(RecipeNamesActivity.this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://food2fork.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        itemName = getIntent().getStringExtra(MainActivity.ITEM_NAME);


        final RecipeApi foodAPI = retrofit.create(RecipeApi.class);
        final LinearLayout linearLayout = findViewById(R.id.namesLinearLayout);

        Call<SearchResult> call =
                foodAPI.getNames(apiid, itemName, "r");

        call.enqueue(new Callback<SearchResult>() {


            @Override
            public void onResponse(Call<SearchResult> call, final Response<SearchResult> response) {
                if (response.body().getCount() >= 10) {
                    int size = response.body().getRecipes().size();
                    for(int i =0; i < size; i ++){
                        TextView recipe = new TextView(RecipeNamesActivity.this);
                        recipe.setText(response.body().getRecipes().get(i).getTitle());
                        ((LinearLayout) linearLayout).addView(recipe);
                        final int finalI = i;
                        recipe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String recipeid = response.body().getRecipes().get(finalI).getRecipeId();
                                showRecipeDetailsActivity(recipeid);
                            }
                        });

                    }

                } else {

                    error.setText("Please check formatting / choose different ingredients");
                }
            }


            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                error.setText(t.getMessage());
            }
        });


    }

    public void showRecipeDetailsActivity(String id){
        Intent newIntent = new Intent(RecipeNamesActivity.this, RecipeDetailsActivity.class);
        newIntent.putExtra(RECIPE_ID, id);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(newIntent);
    }

}