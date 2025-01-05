package eu.kpgtb.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import dev.projectenhanced.enhancedjda.controller.data.persister.custom.GsonPersister;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Date;

@DatabaseTable
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class UserMessage {
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField
    private String user;
    @DatabaseField
    private String message;
    @DatabaseField
    private Date date;
    @DatabaseField
    private Guild guild;
}
