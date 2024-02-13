package edu.brown.cs.student.main.csv.load;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;

public class LoadAPIUtilities {
  public static LoadCSV deserializeLoadCSV(String jsonLoadCSV) {
    try {
      // Initializes Moshi
      Moshi moshi = new Moshi.Builder().build();

      // Initializes an adapter to an Activity class then uses it to parse the JSON.
      JsonAdapter<LoadCSV> adapter = moshi.adapter(LoadCSV.class);

      LoadCSV loadCSV = adapter.fromJson(jsonLoadCSV);

      return loadCSV;
    }
    // Returns an empty activity... Probably not the best handling of this error case...
    // Notice an alternative error throwing case to the one done in OrderHandler. This catches
    // the error instead of pushing it up.
    catch (IOException e) {
      e.printStackTrace();
      return new LoadCSV("", false);
    }
  }
}
