package com.example.quizz.topic;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.quizz.AppDatabase;
import java.util.Objects;

@Entity(tableName = AppDatabase.TOPIC_TABLE)
public class Topic {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String topic;

    public Topic(String topic) {
        this.topic = topic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic1 = (Topic) o;
        return id == topic1.id && Objects.equals(topic, topic1.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic);
    }
}
