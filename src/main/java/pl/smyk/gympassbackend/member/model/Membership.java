package pl.smyk.gympassbackend.member.model;

import com.google.zxing.WriterException;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import pl.smyk.gympassbackend.member.util.CodeGenerator;

import java.io.IOException;
import java.time.LocalDate;

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
    private String qrCode;
    private String userId;
    private MembershipType membershipType;

    public void generateCodes(CodeGenerator codeGenerator, String userId) {
        try {
            this.qrCode = codeGenerator.generateQRCode(userId);
            this.userId = userId;
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return (startDate != null && endDate != null && !today.isBefore(startDate) && !today.isAfter(endDate));
    }
}
