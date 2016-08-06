/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package winnipegtransit;

import base.Geographic;
import base.Ride;
import base.ServerLocation;
import base.Stop;
import base.TimeStamp;
import base.TreePlan;
import base.Trip;
import base.Walk;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MVCArray;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import com.lynden.gmapsfx.shapes.Polyline;
import com.lynden.gmapsfx.shapes.PolylineOptions;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.record.Location;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author nika
 */
public class WinnipegTransit extends Application implements MapComponentInitializedListener {

    GoogleMapView mapView;
    GoogleMap map;
    LatLong ll, startpoint, stoppoint;
String API_KEY="PUT UR KEY HERE";
    String url = "http://api.winnipegtransit.com/v2/trip-planner.json?origin=geo/</LAT>,</LONG>&destination=geo/</DLAT>,</DLONG>&walk-speed=5.3&api-key="+API_KEY;
    String url2 = "http://api.winnipegtransit.com/v2/stops.json?variant=</CODE>&api-key="+API_KEY;
    Marker startMarker, stopMarker;
    ArrayList<Trip> plans = new ArrayList<>();
    ArrayList<Polyline> polylines = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {

        //Create the JavaFX component and set this as a listener so we know when 
        //the map has been initialized, at which point we can then begin manipulating it.
        mapView = new GoogleMapView();
        mapView.addMapInializedListener(this);
        SplitPane sp = new SplitPane();
        final ScrollPane sp1 = new ScrollPane();
        //ListView<String> stoplist = new ListView<>();
        sp1.setFitToHeight(true);
        sp1.setFitToWidth(true);
        TreeItem<String> rootItem = new TreeItem<String>("Plans");
        rootItem.setExpanded(true);
//        ObservableList<String> stopitems = FXCollections.observableArrayList(
//                "Single", "Double", "Suite", "Family App");
//       // stoplist.setItems(stopitems);

        //rootItem.getChildren().addAll(new TreeItem<>("Plan1"));
        TreeView tv = new TreeView(rootItem);
        //tv.setMinWidth(50);
        tv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                    Object newValue) {
                if (oldValue != null) {
                    TreeItem<String> selectedItem2 = (TreeItem<String>) oldValue;
                    selectedItem2.setExpanded(false);
// do what ever you want 
                }
                for (int i = 0; i < polylines.size(); i++) {
                    map.removeMapShape(polylines.get(i));

                }
                polylines.clear();
                int zoom = map.getZoom();
                map.setZoom(zoom + 1);
                map.setZoom(zoom);
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                selectedItem.setExpanded(true);
                String pln = getPlanNo(selectedItem).getValue();
                int pno = Integer.parseInt(pln.substring(0, pln.indexOf(".")).trim());
                Trip st = plans.get(pno - 1);
                for (int i = 0; i < st.getWalks().size(); i++) {
                    Walk w = st.getWalks().get(i);
                    LatLong[] ary = new LatLong[]{w.getSource().toLatLong(), w.getDestination().toLatLong()};
                    MVCArray mvc = new MVCArray(ary);

                    PolylineOptions plo = new PolylineOptions().path(mvc).strokeColor("green").strokeWeight(2);
                    Polyline plne = new Polyline(plo);
                    map.addMapShape(plne);
                    polylines.add(plne);
                }

                for (int i = 0; i < st.getRides().size(); i++) {
                    Ride rd = st.getRides().get(i);
                    rd.getStops().size();
                    LatLong[] ary = new LatLong[rd.getStops().size()];
                    for (int j = 0; j < rd.getStops().size(); j++) {
                        ary[j] = rd.getStops().get(j).getGeographic().toLatLong();
                        System.out.println(i + " Stop " + j + " LOC " + rd.getStops().get(j).getGeographic().toString());
                    }
                    MVCArray mvc = new MVCArray(ary);

                    PolylineOptions plo = new PolylineOptions().path(mvc).strokeColor("blue").strokeWeight(3);
                    Polyline plne = new Polyline(plo);
                    map.addMapShape(plne);

                    polylines.add(plne);
                }

            }

        });
        sp1.setContent(new BorderPane(tv));
        final ScrollPane sp2 = new ScrollPane();
        ListView<String> buslist = new ListView<>();
        ObservableList<String> busitems = FXCollections.observableArrayList(
                "Single1", "Double1", "Suite1", "Family App1");
        buslist.setItems(busitems);

        //sp2.setContent(buslist);
        final ContextMenu contextMenu = new ContextMenu();
//        contextMenu.setOnShowing((WindowEvent e) -> {
//            System.out.println("showing");
//        });
//        contextMenu.setOnShown((WindowEvent e) -> {
//            System.out.println("shown");
//        });

        MenuItem item1 = new MenuItem("Start here");
        MenuItem item2 = new MenuItem("Stop Here");
        item1.setOnAction((ActionEvent e) -> {
            startpoint = ll;
            item2.setDisable(false);
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(startpoint)
                    .visible(Boolean.TRUE)
                    .title("Start Here");

            if (startMarker != null) {
                map.removeMarker(startMarker);
                startMarker = null;

                int zoom = map.getZoom();
                map.setZoom(zoom + 1);
                map.setZoom(zoom);

            }
            startMarker = new Marker(markerOptions);

            map.addMarker(startMarker);

        });
        item2.setDisable(true);
        item2.setOnAction((ActionEvent e) -> {
            stoppoint = ll;
            plans.clear();
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions.position(stoppoint)
                    .visible(Boolean.TRUE)
                    .title("Stop");
            if (stopMarker != null) {
                map.removeMarker(stopMarker);
                stopMarker = null;
                int zoom = map.getZoom();
                map.setZoom(zoom + 1);
                map.setZoom(zoom);

            }
            stopMarker = new Marker(markerOptions);

            map.addMarker(stopMarker);
            System.out.println("Start " + startpoint.toString() + " Stop" + stoppoint.toString());
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
                    stopAction(rootItem);
//                }
//            });
//            t.start();
        });
        contextMenu.getItems().addAll(item1, item2);

        mapView.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(mapView, e.getScreenX(), e.getScreenY());
            } else if (contextMenu.isShowing()) {
                contextMenu.hide();
            }
        });

        sp.getItems().addAll(mapView, sp1);//, sp2
        sp.setDividerPositions(0.7f);//0.85f
        Scene scene = new Scene(sp);
        stage.setTitle("JavaFX and Google Maps");
        stage.setScene(scene);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        stage.show();
    }

    TreeItem<String> getPlanNo(TreeItem<String> t) {
        if (t.getParent().getValue().equalsIgnoreCase("Plans")) {
            return t;
        } else {
            return getPlanNo(t.getParent());
        }
    }

    JSONObject getLatLng(JSONObject src) {
        if (src.has("origin")) {
            return src.getJSONObject("origin").getJSONObject("point").getJSONObject("centre").getJSONObject("geographic");
        } else if (src.has("stop")) {
            return src.getJSONObject("stop").getJSONObject("centre").getJSONObject("geographic");

        } else if (src.has("destination")) {
            return src.getJSONObject("destination").getJSONObject("point").getJSONObject("centre").getJSONObject("geographic");

        }
        return null;
    }

    void stopAction(TreeItem rootItem) {
        JSONObject jsb = getPlans(startpoint, stoppoint);
        System.out.println("" + jsb.toString());
        JSONArray plansJ = (jsb.getJSONArray("plans"));
        for (int i = 0; i < plansJ.length(); i++) {
            Trip trp = new Trip(new Geographic(startpoint.getLatitude(), startpoint.getLongitude()), new Geographic(stoppoint.getLatitude(), stoppoint.getLongitude()));
            JSONArray segments = (plansJ.getJSONObject(i)).getJSONArray("segments");
            TreePlan tp = (new TreePlan(rootItem));
            JSONObject dur = (plansJ.getJSONObject(i)).getJSONObject("times").getJSONObject("durations");
            tp.getPlan().getChildren().add(new TreeItem<>("Total Durating- " + dur.getInt("total") + " mins"));
            tp.getPlan().getChildren().add(new TreeItem<>("\tWalking- " + dur.getInt("walking") + " mins"));

            tp.getPlan().getChildren().add(new TreeItem<>("\tWaiting- " + dur.getInt("waiting") + " mins"));
            tp.getPlan().getChildren().add(new TreeItem<>("\tRiding- " + dur.getInt("riding") + " mins"));

            int ride = 0;
            ArrayList<Ride> rides = new ArrayList<>();
            ArrayList<Walk> wlks = new ArrayList<>();
            for (int j = 0; j < segments.length(); j++) {
                JSONObject seg = segments.getJSONObject(j);
                String type = seg.getString("type");
                if (type.equalsIgnoreCase("ride")) {
                    JSONObject stops = null;
                    ArrayList<Stop> stps = new ArrayList<>();
                    try {
                        stops = readJsonFromUrl(url2.replace("</CODE>", "" + seg.getJSONObject("variant").getString("key")));
                    } catch (IOException | JSONException ex) {
                        Logger.getLogger(WinnipegTransit.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JSONArray stopsArray = stops.getJSONArray("stops");

                    for (int k = 0; k < stopsArray.length(); k++) {
                        JSONObject object = stopsArray.getJSONObject(k);
                        stps.add(new Stop("" + object.getLong("key"), "" + object.getLong("number"), object.getString("name"), object.getString("direction"), new Geographic(object.getJSONObject("centre").getJSONObject("geographic").getBigDecimal("latitude").doubleValue(), object.getJSONObject("centre").getJSONObject("geographic").getBigDecimal("longitude").doubleValue())));
                    }
                    JSONObject from = (seg.getJSONObject("bounds").getJSONObject("minimum"));
                    JSONObject to = (seg.getJSONObject("bounds").getJSONObject("maximum"));
//                        JSONObject from = (seg.getJSONObject("from"));
//                        JSONObject to = (seg.getJSONObject("to"));
//                        JSONObject fromgeo = (getLatLng(from));
//                        JSONObject togeo = (getLatLng(to));

                    System.out.println("Ride: " + seg.toString());
                    Ride rd = new Ride(new Geographic(from.getBigDecimal("lat").doubleValue(), from.getBigDecimal("lng").doubleValue()), new Geographic(to.getBigDecimal("lat").doubleValue(), to.getBigDecimal("lng").doubleValue()));
                    rd.setStops(stps);
                    trp.setSeq(trp.getSeq().concat("R,"));
                    System.out.println("From " + rd.getSource().toString() + " to" + rd.getDestination().toString());

                    if (ride == 0) {
                        tp.setBusNo(((i + 1) + ". " + seg.getJSONObject("route").getInt("number")));
                        tp.setDepartureTime(new TimeStamp(seg.getJSONObject("times").getString("start")).getDate());
                        tp.setEndTime(new TimeStamp(seg.getJSONObject("times").getString("end")).getDate());
                    } else {
                        tp.setBusNo(tp.getBusNo().concat("->" + seg.getJSONObject("route").getInt("number")));
                    }
                    rides.add(rd);
                    ride++;
                } else if (type.equalsIgnoreCase("walk")) {
//                        JSONObject from = (seg.getJSONObject("bounds").getJSONObject("minimum"));
//                        JSONObject to = (seg.getJSONObject("bounds").getJSONObject("maximum"));
                    JSONObject from = (seg.getJSONObject("from"));
                    JSONObject to = (seg.getJSONObject("to"));
                    JSONObject fromgeo = (getLatLng(from));
                    JSONObject togeo = (getLatLng(to));
                    System.out.println(" Debug From: " + fromgeo.toString());
                    System.out.println(" Debug To: " + togeo.toString());
                    trp.setSeq(trp.getSeq().concat("W,"));

                    Walk wlk = new Walk(new Geographic(fromgeo.getBigDecimal("latitude").doubleValue(), fromgeo.getBigDecimal("longitude").doubleValue()), new Geographic(togeo.getBigDecimal("latitude").doubleValue(), togeo.getBigDecimal("longitude").doubleValue()), seg.getJSONObject("times").getJSONObject("durations").getLong("walking"), type);
                    wlks.add(wlk);
                }
            }
            trp.setRides(rides);
            trp.setWalks(wlks);

            rootItem.getChildren().add(tp.getPlan());
            plans.add(trp);
        }

    }

    JSONObject getPlans(LatLong src, LatLong dst) {
        JSONObject jso = null;
        try {

            String turl = url;
            turl = turl.replace("</LAT>", "" + src.getLatitude());
            turl = turl.replace("</LONG>", "" + src.getLongitude());
            turl = turl.replace("</DLAT>", "" + dst.getLatitude());
            turl = turl.replace("</DLONG>", "" + dst.getLongitude());
            jso = readJsonFromUrl(turl);
        } catch (IOException | JSONException ex) {
            Logger.getLogger(WinnipegTransit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jso;
    }

    @Override
    public void mapInitialized() {
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();

        mapOptions.center(new LatLong(49.8897, -97.1531))
                .mapType(MapTypeIdEnum.ROADMAP)
                .overviewMapControl(true)
                .panControl(true)
                .rotateControl(true)
                .scaleControl(true)
                .streetViewControl(false)
                .zoomControl(true)
                .zoom(12);

        map = mapView.createMap(mapOptions);

        //Add a marker to the map
        System.out.println("IP : " + getIP());
        //  JSONObject json = new JSONObject(readUrl(STYLESHEET_MODENA));
//mapView.onMouseClickedProperty().addListener();
        map.addUIEventHandler(UIEventType.rightclick, (JSObject obj) -> {
            ll = new LatLong((JSObject) obj.getMember("latLng"));
            System.out.println("LatLong: lat: " + ll.getLatitude() + " lng: "
                    + ll.getLongitude());
//     lblClick.setText(ll.toString());
        });
    }

    String getIP() {
        String ip = null;
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            ip = in.readLine(); //you get the IP as a String
            System.out.println(ip);
        } catch (MalformedURLException ex) {
            Logger.getLogger(WinnipegTransit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WinnipegTransit.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ip;
    }

    private static String readUrl(String urlString) {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } catch (MalformedURLException ex) {
            Logger.getLogger(WinnipegTransit.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WinnipegTransit.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(WinnipegTransit.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /*
    public ServerLocation getLocation(String ipAddress) {

	File file = new File(
	    "GeoLiteCity.dat");
	return getLocation(ipAddress, file);

  }

  public ServerLocation getLocation(String ipAddress, File file) {

	ServerLocation serverLocation = null;

	try {

	serverLocation = new ServerLocation();
            DatabaseReader db= new DatabaseReader();
	LookupService lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
	Location locationServices = lookup.getLocation(ipAddress);

	serverLocation.setCountryCode(locationServices.countryCode);
	serverLocation.setCountryName(locationServices.countryName);
	serverLocation.setRegion(locationServices.region);
	serverLocation.setRegionName(regionName.regionNameByCode(
             locationServices.countryCode, locationServices.region));
	serverLocation.setCity(locationServices.city);
	serverLocation.setPostalCode(locationServices.postalCode);
	serverLocation.setLatitude(String.valueOf(locationServices.latitude));
	serverLocation.setLongitude(String.valueOf(locationServices.longitude));

	} catch (IOException e) {
		System.err.println(e.getMessage());
	}

	return serverLocation;

  }
     */
    public static void main(String[] args) {
        launch(args);
    }
}
