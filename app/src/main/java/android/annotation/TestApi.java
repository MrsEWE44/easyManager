package android.annotation;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Indicates an API is exposed for use by CTS.
 * <p>
 * These APIs are not guaranteed to remain consistent release-to-release,
 * and are not for use by apps linking against the Android SDK.
 * </p>
 *
 * @hide
 */
@Target({TYPE, FIELD, METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PACKAGE})
@Retention(RetentionPolicy.SOURCE)
public @interface TestApi {
}