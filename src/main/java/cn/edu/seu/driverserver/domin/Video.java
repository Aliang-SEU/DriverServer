package cn.edu.seu.driverserver.domin;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Video {

    @NonNull
    private int videoId;

    @NonNull
    private int deviceId;

    @NonNull
    private String videoUrl;

    @NonNull
    private int type;

    @NonNull
    private String createTime;


}