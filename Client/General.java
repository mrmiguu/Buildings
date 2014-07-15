import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

final
class General {

/************************************************
 * Attempts to alter the pre-existing value of  *
 * a private static final field inside of a     *
 * class.                                       *
 *                                              *
 * @param c                                     *
 *      the field's outer class                 *
 * @param field                                 *
 *      the private static final field to       * 
 *      change                                  *
 * @param value                                 *
 *      the value to reset the private static   *
 *      final field to                          *
 ************************************************/
static
void setImmutable(
final Class<?> c,
final String field,
final Object value) {

    try {

    final Field f = c.getDeclaredField(field);
    f.setAccessible(true);

    final Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);

    f.set(null, value);
    }
    catch (final IllegalAccessException e) { e.printStackTrace(); }
    catch (final NoSuchFieldException e) { e.printStackTrace(); }
    catch (final Exception e) { e.printStackTrace(); }
}
}