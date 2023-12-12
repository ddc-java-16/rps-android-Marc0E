package edu.cnm.deepdive.rps.hilt;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import java.security.SecureRandom;
import java.util.Random;
import org.apache.commons.rng.simple.JDKRandomBridge;
import org.apache.commons.rng.simple.RandomSource;

/**
 * Assist Hilt in satisfying dependencies on classes that it cannot otherwise instantiate.
 */
@Module
@InstallIn(SingletonComponent.class)
public final class RandomModule {

  /**
   * Creates and returns an instance of {@link Random} (or a subclass) for use when a component
   * requires a source of randomness as a dependency.
   */
  @Provides
  public Random provideRandom() {
    return new JDKRandomBridge(RandomSource.XO_RO_SHI_RO_128_PP, null);
  }

}
