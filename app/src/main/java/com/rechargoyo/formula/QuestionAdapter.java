package com.rechargoyo.formula;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final ArrayList<Question> questionList;

    public QuestionAdapter(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewQuestion;
        private final RadioGroup radioGroupAnswers;
        private final RadioButton[] radioButtons = new RadioButton[4];

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            radioGroupAnswers = itemView.findViewById(R.id.radioGroupAnswers);
            radioButtons[0] = itemView.findViewById(R.id.radioButton1);
            radioButtons[1] = itemView.findViewById(R.id.radioButton2);
            radioButtons[2] = itemView.findViewById(R.id.radioButton3);
            radioButtons[3] = itemView.findViewById(R.id.radioButton4);
        }

        public void bind(Question question) {
            textViewQuestion.setText(question.getQuestionText());
            ArrayList<String> answers = question.getAnswers();
            for (int i = 0; i < radioButtons.length; i++) {
                radioButtons[i].setText(answers.get(i));
                radioButtons[i].setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int selectedAnswer = radioGroupAnswers.indexOfChild(v);
                        question.setSelectedOptionIndex(selectedAnswer);
                    }
                });
            }
        }
    }
}
