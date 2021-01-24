package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

/**
 * this is Question entity class
 * @Author M.DHANUSRI
 */

@SuppressWarnings("all")
@Entity
@Table(name = "question")
@NamedQueries(
        {
                @NamedQuery(
                        name = "getQuestionById",
                        query = "select q from QuestionEntity q where q.uuid = :uuid"),
                @NamedQuery(
                        name = "getQuestionByUser",
                        query = "select q from QuestionEntity q where q.userEntity=:user"),
                @NamedQuery(name = "allQuestions", query = "select q from QuestionEntity q"),

                //@Vipin P K: Added query to get the question using uuid..to implement in createanswer
                @NamedQuery(name = "questionEntityByUuid",
                        query = "select qe from QuestionEntity qe where qe.uuid = :uuid"),

        }
)
public class QuestionEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uuid")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "content", columnDefinition = "text")
    @Size(max = 500)
    @NotNull
    private String content;


    @Column(name = "date")
    @NotNull
    private ZonedDateTime date;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}
