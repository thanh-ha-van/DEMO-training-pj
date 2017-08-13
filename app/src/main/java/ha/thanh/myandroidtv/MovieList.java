package ha.thanh.myandroidtv;

import java.util.ArrayList;
import java.util.List;

public final class MovieList {

    public static List<Movie> list;

    public static List<Movie> setupMovies() {
        list = new ArrayList<>();
        String title[] = {
                "Stream file 1",
                "Stream file 2",
                "Stream file 3",
                "PM4 file 1",
                "PM4 file 2",
                "PM4 file 3",
        };

        String description = "description";

        String videoUrl[] = {
                "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8",
                "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8",
                "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_16x9/bipbop_16x9_variant.m3u8",
                "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4",
                "http://techslides.com/demos/sample-videos/small.mp4",
                "http://demos.webmproject.org/exoplayer/glass.mp4",
        };
        String bgImageUrl[] = {
                "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg",
                "https://cdn.pixabay.com/photo/2016/02/21/00/26/walnut-1213037_960_720.jpg",
                "http://barkpost-assets.s3.amazonaws.com/wp-content/uploads/2013/11/muchdoge-700x393.jpg",
                "http://keenthemes.com/preview/metronic/theme/assets/global/plugins/jcrop/demos/demo_files/image1.jpg",
                "http://i0.kym-cdn.com/entries/icons/mobile/000/013/564/doge.jpg",
                "https://i.ytimg.com/vi/Yj7ja6BANLM/maxresdefault.jpg",

        };
        String cardImageUrl[] = {
                "https://cdn.pixabay.com/photo/2013/04/06/11/50/image-editing-101040_960_720.jpg",
                "https://cdn.pixabay.com/photo/2016/02/21/00/26/walnut-1213037_960_720.jpg",
                "http://barkpost-assets.s3.amazonaws.com/wp-content/uploads/2013/11/muchdoge-700x393.jpg",
                "http://keenthemes.com/preview/metronic/theme/assets/global/plugins/jcrop/demos/demo_files/image1.jpg",
                "http://i0.kym-cdn.com/entries/icons/mobile/000/013/564/doge.jpg",
                "https://i.ytimg.com/vi/Yj7ja6BANLM/maxresdefault.jpg",
        };

        for (int i = 0; i < 6; i ++) {
            list.add(buildMovieInfo("category",
                    title[i],
                    description,
                    "Studio " + i,
                    videoUrl[i],
                    cardImageUrl[i],
                    bgImageUrl[i]));

        }

        return list;
    }

    private static Movie buildMovieInfo(String category,
                                        String title,
                                        String description,
                                        String studio,
                                        String videoUrl,
                                        String cardImageUrl,
                                        String bgImageUrl) {
        Movie movie = new Movie();
        movie.setId(Movie.getCount());
        Movie.incCount();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCategory(category);
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl(bgImageUrl);
        movie.setVideoUrl(videoUrl);
        return movie;
    }
}
