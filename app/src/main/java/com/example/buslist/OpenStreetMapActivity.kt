package com.example.buslist

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.Marker
import org.json.JSONObject
import java.io.InputStream

class OpenStreetMapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var descriptions: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_osm)

        // Initialize OSMDroid configuration
        Configuration.getInstance().load(applicationContext, getPreferences(MODE_PRIVATE))

        mapView = findViewById(R.id.mapView)
        mapView.setUseDataConnection(true) // Set to false for offline mode
        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE) // Or TileSourceFactory.MAPNIK
        mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT)
        mapView.setMultiTouchControls(true)

        val stationName = intent.getStringExtra("stationName")
        val routeJsonFileName = stationName?.let { getJsonFileNameForStation(it) }
        val routeJsonContent: String? = routeJsonFileName?.let { loadJSONFromAsset(it) }

        val stationsJsonFileName = "selitastations.json"
        val stationsJsonContent: String? = loadJSONFromAsset(stationsJsonFileName)

        if (routeJsonContent != null && stationsJsonContent != null) {
            val (parsedLines, parsedDescriptions) = parseRouteJSON(routeJsonContent)
            descriptions = parsedDescriptions

            parsedLines.forEach { lineCoordinates ->
                val polyline = Polyline()
                lineCoordinates.forEach { (lat, lon) ->
                    polyline.addPoint(GeoPoint(lat, lon))
                }
                polyline.color = -0x10000 // Set your preferred color
                polyline.width = 10.0f // Set your preferred line width
                mapView.overlays.add(polyline)
            }

            val parsedWaypoints = parseStationsJSON(stationsJsonContent)
            addMarkersToMap(mapView, parsedWaypoints, descriptions)

            if (parsedLines.isNotEmpty()) {
                val firstLine = parsedLines.first()
                val firstPoint = firstLine.first()
                mapView.controller.setCenter(GeoPoint(firstPoint.first, firstPoint.second))
                mapView.controller.setZoom(16.0)
                mapView.tileProvider.clearTileCache()
                mapView.invalidate()
            }
        }
    }

    private fun getJsonFileNameForStation(stationName: String): String {
        return when (stationName) {
            "Kombinat - Kinostudio" -> "kombinat_kinostudio.json"
            "Tirana e Re" -> "tirana_re.json"
            "Unaza" -> "unaza.json"
            "Selite" -> "selita.json" //
            "Porcelan-Qender" -> "porcelan_qender.json"
            "Sauk - Qender" -> "sauk_qender.json"
            "Uzina Traktori - Qender" -> "uzinatraktori_qender.json"
            "Instituti Bujqesor - Qender" -> "institut_qender.json" //
            "St Trenit - Qender - TEG" -> "treni_qender_teg.json" //
            "Tufine - Kombinat" -> "tufine_kombinat.json"
            "Sharre - Uzina Dinamo" -> "sharre_uzinadinamo.json"
            else -> "default.json" // Replace with your default JSON file
        }
    }

    private fun parseStationsJSON(json: String): List<Pair<Double, Double>> {
        val parsedWaypoints = mutableListOf<Pair<Double, Double>>()

        try {
            val jsonObject = JSONObject(json)
            val featuresArray = jsonObject.getJSONArray("features")

            for (i in 0 until featuresArray.length()) {
                val feature = featuresArray.getJSONObject(i)
                val geometry = feature.getJSONObject("geometry")
                val type = geometry.getString("type")

                if (type == "Feature") {
                    val coordinates = geometry.getJSONArray("coordinates")
                    val lon = coordinates.getDouble(0)
                    val lat = coordinates.getDouble(1)

                    parsedWaypoints.add(Pair(lat, lon))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return parsedWaypoints
    }


    private fun parseRouteJSON(json: String): Pair<List<List<Pair<Double, Double>>>, List<String>> {
        val parsedLines = mutableListOf<List<Pair<Double, Double>>>()
        val parsedDescriptions = mutableListOf<String>()

        try {
            val jsonObject = JSONObject(json)
            val featuresArray = jsonObject.getJSONArray("features")

            for (i in 0 until featuresArray.length()) {
                val feature = featuresArray.getJSONObject(i)
                val geometry = feature.getJSONObject("geometry")
                val type = geometry.getString("type")

                if (type == "LineString") {
                    val coordinatesArray = geometry.getJSONArray("coordinates")
                    val lineCoordinates = mutableListOf<Pair<Double, Double>>()

                    for (j in 0 until coordinatesArray.length()) {
                        val coordinate = coordinatesArray.getJSONArray(j)
                        val lon = coordinate.getDouble(0)
                        val lat = coordinate.getDouble(1)
                        lineCoordinates.add(Pair(lat, lon))
                    }

                    parsedLines.add(lineCoordinates)
                    parsedDescriptions.add(feature.getJSONObject("properties").getString("description"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Pair(parsedLines, parsedDescriptions)
    }

    private fun addMarkersToMap(mapView: MapView, waypoints: List<Pair<Double, Double>>, descriptions: List<String>) {
        for (i in waypoints.indices) {
            val (lat, lon) = waypoints[i]
            val description = if (i < descriptions.size) descriptions[i] else ""

            val marker = Marker(mapView)
            marker.position = GeoPoint(lat, lon)
            marker.title = description
            mapView.overlays.add(marker)
        }
        Log.d("Debugging", "Markers added: ${waypoints.size}")
    }

    private fun loadJSONFromAsset(fileName: String): String? {
        var jsonContent: String? = null
        try {
            val inputStream: InputStream = assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            jsonContent = String(buffer, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonContent
    }
}
