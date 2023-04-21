package com.yesipov.gusto.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesipov.gusto.Adapters.FoodAdapter;
import com.yesipov.gusto.Adapters.SpecialsAdapter;
import com.yesipov.gusto.Models.Food;
import com.yesipov.gusto.Models.Specials;
import com.yesipov.gusto.R;
import com.yesipov.gusto.SpecialList;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference refFood = FirebaseDatabase.getInstance().getReference("food");
    DatabaseReference refSpec = FirebaseDatabase.getInstance().getReference("specials");
    DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");
    DatabaseReference refWorkers = FirebaseDatabase.getInstance().getReference("workers");

    TextView points, name;
    RecyclerView listFoods;
    RecyclerView recyclerViewPizza;

    String pointsFromDB, nameFromDB;
    List<Food> listFoodsFromClass = new ArrayList<Food>();
    List<String> names = new ArrayList<String>();

    public MenuFragment() {}

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerViewPizza = (RecyclerView) getView().findViewById(R.id.pizza_list);
        setListSpecials();
        setListPizza();
    }

    void setListSpecials() {

        ArrayList<Specials> specials = new ArrayList<Specials>();
        specials.add(new Specials("Успеть за 60 минут",
                    "Доставка в течение 60 минут или пицца в подарок! Мы заботимся о своих клиентах. Поэтому, если курьер приедет позже указанного времени, то мы подарим вам сертификат с промокодом на пиццу диаметром 30 см на тонком тесте. Активируйте промокод или озвучьте его оператору call center и получите любую пиццу 30 см в подарок при заказе на минимальную стоимость. Промокод будет применён к пицце с наименьшей стоимостью в заказе. Предложение действует при заказе на доставку. Не действует на раздаче в ресторане и самовывоз. Акция не суммируется с бонусной программой, другими акциями и спецпредложениями.",
                    R.drawable.special_2));
        specials.add(new Specials("Самовывоз",
                "Теперь при заказе на самовывоз мы дарим скидку 15 % на пиццу!",
                R.drawable.special_1));
        specials.add(new Specials("Бонусная программа", "Бонусная программа действует в ресторанах и службе доставки.\n" +
                "\n" +
                "5% от каждого заказа будет возвращаться вам на счет в виде бонусов, которыми Вы можете оплатить до 30% от суммы последующих покупок.   \n" +
                "\n" +
                "Вы можете оформить заказ на сайте, в мобильном приложении или позвонить в колл-центр. Для списания бонусов вам понадобится мобильный телефон.",
                R.drawable.special_3));

        refSpec.setValue(specials).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });



        refSpec.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Specials> specials = new ArrayList<Specials>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    specials.add(child.getValue(Specials.class));
                }

                RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.special_list);
                SpecialsAdapter.OnSpecialClickListener specialClickListener = new SpecialsAdapter.OnSpecialClickListener() {
                    @Override
                    public void onSpecialClick(Specials special, int position) {
                        Intent intent = new Intent(getActivity(), SpecialList.class);
                        intent.putExtra("image", special.getFlagResource());
                        intent.putExtra("title", special.getTitle());
                        intent.putExtra("describe", special.getDescriber());
                        startActivity(intent);
                    }
                };

                SpecialsAdapter adapter = new SpecialsAdapter(getContext(), specials, specialClickListener);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    void setListPizza() {
        ArrayList<Food> pizza = new ArrayList<Food>();
        pizza.add(new Food("Пепперони", "Пицца", "сыр Моцарелла, пепперони, соус Томатный", 415, 460, R.drawable.pizza_1));
        pizza.add(new Food("Маргарита", "Пицца", "помидоры, орегано, сыр Моцарелла, соус Томатный, соус Маджорио", 415, 460, R.drawable.pizza_8));
        pizza.add(new Food("Острая с беконом", "Пицца", "халапеньо, бекон, соус Маджорио, пепперони, помидоры, соус Томатный", 545, 380, R.drawable.pizza_3));
        pizza.add(new Food("Четыре сезона", "Пицца", "ветчина, соус, сыр Моцарелла, шампиньоны\n", 875, 820, R.drawable.pizza_7));
        pizza.add(new Food("Примавера", "Пицца", "шампиньоны, брокколи, перец, сыр Брынза, маслины, орегано, сыр Моцарелла, помидоры, соус Томатный", 460, 515, R.drawable.pizza_5));
        pizza.add(new Food("Дижонская", "Пицца", "лук жареный, Горчичный соус, сервелат, ветчина, маринованные огурцы, сыр Моцарелла, соус Томатный\n", 545, 450, R.drawable.pizza_4));
        pizza.add(new Food("Капричоза", "Пицца", "ветчина, шампиньоны, маринованные огурцы, маслины, сыр Моцарелла, соус Томатный, соус Маджорио, орегано\n", 515, 450, R.drawable.pizza_6));
        pizza.add(new Food("Венеция", "Пицца", "куриное филе, сыр Моцарелла, соус Томатный, соус Маджорио, укроп\n", 545, 460, R.drawable.pizza_2));
        pizza.add(new Food("Гавайская", "Пицца", "куриное филе, ананас, болгарский перец, помидоры, сыр Моцарелла, соус Маджорио", 415, 460, R.drawable.pizza_9));

        refFood.child("pizza").setValue(pizza).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

        ArrayList<Food> drink = new ArrayList<Food>();
        drink.add(new Food("Pepsi 0,5л", "Напитки", "", 99, 500, R.drawable.drink_1));
        drink.add(new Food("7UP 0,5л", "Напитки", "", 99, 500, R.drawable.drink_2));
        drink.add(new Food("Mirinda 0,5л", "Напитки", "", 99, 500, R.drawable.drink_3));
        drink.add(new Food("Квас", "Напитки", "", 95, 500, R.drawable.drink_4));
        drink.add(new Food("Aqua Minerale с газом 0,5л", "Напитки", "", 95, 500, R.drawable.drink_5));

        refFood.child("drinks").setValue(drink).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
 
            }
        });

        ArrayList<Food> refrigs = new ArrayList<Food>();
        refrigs.add(new Food("Крылышки барбекю", "Закуска", "Куриные крылышки, маринад", 215, 250, R.drawable.refrigo_2));
        refrigs.add(new Food("Наггетсы 9 штук", "Закуска", "Куриные наггетсы", 145, 180, R.drawable.refrigo_1));

        refFood.child("refrigs").setValue(refrigs).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

        ArrayList<Food> deserts = new ArrayList<Food>();
        deserts.add(new Food("Чизкейк Нью-Йорк", "Десерт", "", 125, 125, R.drawable.desert_1));
        deserts.add(new Food("Маффин шоколадный", "Десерт", "", 65, 65, R.drawable.desert_2));
        deserts.add(new Food("Брауни карамель", "Десерт", "", 145, 150, R.drawable.desert_3));

        refFood.child("deserts").setValue(deserts).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

/*        List workers = new ArrayList<String>();
        workers.add("3QHi5veOuDNJlnqke6nnKYvSCsb2");

        refWorkers.setValue(workers).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });*/

        refFood.child("pizza").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Food> pizza = new ArrayList<Food>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    pizza.add(child.getValue(Food.class));
                }
                RecyclerView recyclerViewPizza = (RecyclerView) getView().findViewById(R.id.pizza_list);
                FoodAdapter.OnFoodClickListener foodClickListener = new FoodAdapter.OnFoodClickListener() {
                    @Override
                    public void onFoodClick(Food food, int position) {}
                };
                FoodAdapter adapterPizza = new FoodAdapter(getContext(), pizza, foodClickListener);
                recyclerViewPizza.setAdapter(adapterPizza);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        refFood.child("drinks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Food> drink = new ArrayList<Food>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    drink.add(child.getValue(Food.class));
                }
                RecyclerView recyclerViewPizza = (RecyclerView) getView().findViewById(R.id.drinks_list);
                FoodAdapter.OnFoodClickListener foodClickListener = new FoodAdapter.OnFoodClickListener() {
                    @Override
                    public void onFoodClick(Food food, int position) {}
                };
                FoodAdapter adapterDrinks = new FoodAdapter(getContext(), drink, foodClickListener);
                recyclerViewPizza.setAdapter(adapterDrinks);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        refFood.child("deserts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Food> deserts = new ArrayList<Food>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    deserts.add(child.getValue(Food.class));
                }
                RecyclerView recyclerViewPizza = (RecyclerView) getView().findViewById(R.id.desert_list);
                FoodAdapter.OnFoodClickListener foodClickListener = new FoodAdapter.OnFoodClickListener() {
                    @Override
                    public void onFoodClick(Food food, int position) {}
                };
                FoodAdapter adapterDeserts = new FoodAdapter(getContext(), deserts, foodClickListener);
                recyclerViewPizza.setAdapter(adapterDeserts);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        refFood.child("refrigs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Food> refrigs = new ArrayList<Food>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    refrigs.add(child.getValue(Food.class));
                }
                RecyclerView recyclerViewPizza = (RecyclerView) getView().findViewById(R.id.refrigerio_list);
                FoodAdapter.OnFoodClickListener foodClickListener = new FoodAdapter.OnFoodClickListener() {
                    @Override
                    public void onFoodClick(Food food, int position) {}
                };
                FoodAdapter adapterRefregs = new FoodAdapter(getContext(), refrigs, foodClickListener);
                recyclerViewPizza.setAdapter(adapterRefregs);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }
}