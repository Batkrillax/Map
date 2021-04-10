package kz.fis.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    static final LatLng startMarker = new LatLng(52.2974631, 76.92577); // начальная точка - Торайгырова 6
    static final LatLng marker1 = new LatLng(52.2984067, 76.9306645); // Маркер 1 - Белый Ветер
    static final LatLng marker2 = new LatLng(52.2999732, 76.9305433); // Маркер 2 - Лидер
    static final LatLng marker3 = new LatLng(52.2970234, 76.929876); // Маркер 3 - Сарыарка
    static final LatLng marker4 = new LatLng(52.2976388, 76.9279209); // Маркер 4 - Small
    static final LatLng marker5 = new LatLng(52.2973306, 76.9354086); // Маркер 5 - Fix Price
    private static final float ALPHA = 0.85f;
    private final int SITYSCALE = 16;
    private GoogleMap mMap;
    private TextView textView;
    private String markerTitle = "";
    private String markerFileName = ""; // Имя файла с подробными данными выбранного маркера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Доступ к карте
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        textView = findViewById(R.id.textViewInfo); // Доступ к компоненту
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Гибридный тип карты
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // Добавление маркера на карту с текстом
        mMap.addMarker(new MarkerOptions().position(startMarker).title((getString(R.string.startMarker_title))));

        // Добавление маркера на карту с текстом, иконкой и полупрозрачностью
        mMap.addMarker(new MarkerOptions().position(marker1).title(getString(R.string.marker1_title)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.white_wind)).alpha(ALPHA).
                snippet(getString(R.string.marker1_txt_click)));

        mMap.addMarker(new MarkerOptions().position(marker2).title((getString(R.string.marker2_title))).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.leader)).alpha(ALPHA).
                snippet(getString(R.string.marker2_txt_click)));

        mMap.addMarker(new MarkerOptions().position(marker3).title(getString(R.string.marker3_title)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.saryarqa)).alpha(ALPHA).
                snippet(getString(R.string.marker3_txt_click)));

        mMap.addMarker(new MarkerOptions().position(marker4).title((getString(R.string.marker4_title))).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.small)).alpha(ALPHA).
                snippet(getString(R.string.marker4_txt_click)));

        mMap.addMarker(new MarkerOptions().position(marker5).title(getString(R.string.marker5_title)).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.fix_price)).alpha(ALPHA).
                snippet(getString(R.string.marker5_txt_click)));

        //Разрешение изменения масштаба карты
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Проверка на включенный GPS и позиционирование на карте
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //Показать текущее местоположение по GPS
            mMap.setMyLocationEnabled(true);
        }

        // Переход просмотра на карте на нужный маркер c зумом
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startMarker, SITYSCALE));

        // Инициализация стартового маркера
        onMarkerClick(getString(R.string.startMarker_id));

        // Обработчик нажатия на маркеры карты
        mMap.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            MapsActivity.this.onMarkerClick(marker.getId());
            return true;
        });
    }

    // Нажатие на маркер
    public void onMarkerClick(String idMarker) {
        switch (idMarker) {
            case "m0":
                doClickMarker(startMarker, getString(R.string.startMarker_info),
                        getString(R.string.startMarker_title), getString(R.string.startMarker_file));
                break;
            case "m1":
                doClickMarker(marker1, getString(R.string.marker1_info),
                        getString(R.string.marker1_title), getString(R.string.marker1_file));
                break;
            case "m2":
                doClickMarker(marker2, getString(R.string.marker2_info),
                        getString(R.string.marker2_title), getString(R.string.marker2_file));
                break;
            case "m3":
                doClickMarker(marker3, getString(R.string.marker3_info),
                        getString(R.string.marker3_title), getString(R.string.marker3_file));
                break;
            case "m4":
                doClickMarker(marker4, getString(R.string.marker4_info),
                        getString(R.string.marker4_title), getString(R.string.marker4_file));
                break;
            case "m5":
                doClickMarker(marker5, getString(R.string.marker5_info),
                        getString(R.string.marker5_title), getString(R.string.marker5_file));
                break;
        }
    }

    // Обработка нажатия на маркер
    public void doClickMarker(LatLng marker, String info, String markerTitle, String markerFileName) {
        this.markerTitle = markerTitle;
        this.markerFileName = markerFileName;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, SITYSCALE));
        findViewById(R.id.sv1).scrollTo(0, 0);
        textView.setText(Html.fromHtml(info, Html.FROM_HTML_MODE_LEGACY));
    }

    // Нажатие на кнопку маркера
    public void onClickButtonMarker(View view) {
        String idMarker = view.getTag().toString();
        onMarkerClick(idMarker);
    }

    // Обработчик кнопки "Подробно"
    public void detailButtonClick(View view) {
        if (!markerFileName.equals("")) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(getString(R.string.tMarker), markerTitle);
            intent.putExtra(getString(R.string.mfile), markerFileName);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), R.string.selectOb, Toast.LENGTH_SHORT).show();
        }
    }

}
