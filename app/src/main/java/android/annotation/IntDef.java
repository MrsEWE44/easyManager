package android.annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;
/**
 * Denotes that the annotated element of integer type, represents
 * a logical type and that its value should be one of the explicitly
 * named constants. If the {@link #flag()} attribute is set to true,
 * multiple constants can be combined.
 * <p>
 * <pre><code>
 *  &#64;Retention(SOURCE)
 *  &#64;IntDef({NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_TABS})
 *  public @interface NavigationMode {}
 *  public static final int NAVIGATION_MODE_STANDARD = 0;
 *  public static final int NAVIGATION_MODE_LIST = 1;
 *  public static final int NAVIGATION_MODE_TABS = 2;
 *  ...
 *  public abstract void setNavigationMode(@NavigationMode int mode);
 *  &#64;NavigationMode
 *  public abstract int getNavigationMode();
 * </code></pre>
 * For a flag, set the flag attribute:
 * <pre><code>
 *  &#64;IntDef(
 *      flag = true,
 *      value = {NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_TABS})
 * </code></pre>
 *
 * @hide
 */
@Retention(SOURCE)
@Target({ANNOTATION_TYPE})
public @interface IntDef {
    /** Defines the constant prefix for this element */
    String[] prefix() default {};
    /** Defines the constant suffix for this element */
    String[] suffix() default {};
    /** Defines the allowed constants for this element */
    int[] value() default {};
    /** Defines whether the constants can be used as a flag, or just as an enum (the default) */
    boolean flag() default false;
}