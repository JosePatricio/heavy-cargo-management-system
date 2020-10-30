package ec.redcode.tas.on.android.dto.requests;

import lombok.Data;

@Data
public class DocumentRequestDTO {

    private String fileName;
    private String fileType;
    private int fileSize;
    private String file;

}
