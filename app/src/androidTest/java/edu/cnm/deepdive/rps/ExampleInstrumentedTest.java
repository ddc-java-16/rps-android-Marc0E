package edu.cnm.deepdive.rps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.jupiter.api.Test;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {

  @Test
  public void useAppContext() {
    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    assertEquals("edu.cnm.deepdive.rps", appContext.getPackageName());
  }
}