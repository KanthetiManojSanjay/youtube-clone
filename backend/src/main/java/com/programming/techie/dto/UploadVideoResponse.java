package com.programming.techie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author kansanja on 23/11/21.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadVideoResponse {
    private String videoId;
    private String videoUrl;
}
