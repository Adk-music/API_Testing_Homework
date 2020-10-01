import java.util.List;
import java.util.stream.Stream;

public class TestUtils {

    private TestUtils(){
        throw new IllegalArgumentException();
    }

    public static Stream<String> getAvatarFileNameStream(List<String> referencesList) {
        return referencesList.stream()
                .map(reference -> reference.split("/"))
                .map(stringArray -> stringArray[stringArray.length - 1]);
    }
}
