package wcd;

import java.util.Set;

public class WallpaperCollection {

    private final String name;

    private final Set<String> downloadUrls;

    public WallpaperCollection(String name, Set<String> downloadUrls) {
        this.name = name;
        this.downloadUrls = downloadUrls;
    }

    public String getName() {
        return name;
    }

    public Set<String> getDownloadUrls() {
        return downloadUrls;
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
