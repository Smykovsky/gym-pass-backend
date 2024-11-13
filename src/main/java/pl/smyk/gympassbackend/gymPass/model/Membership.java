package pl.smyk.gympassbackend.gymPass.model;

import com.google.zxing.WriterException;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.smyk.gympassbackend.gymPass.util.CodeGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@Document( collection = "memberships")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Membership {
    @MongoId
    private String id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private String qrCode;
    private String barCode;


    public void generateCodes(CodeGenerator codeGenerator) {
        HashMap<String, Object> codeData = new HashMap<>();
        codeData.put("startDate", startDate);
        codeData.put("endDate", endDate);
        codeData.put("isActive", active);

        try {
            this.qrCode = codeGenerator.generateQRCode(codeData);
            this.barCode = codeGenerator.generateBarcode(codeData);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
