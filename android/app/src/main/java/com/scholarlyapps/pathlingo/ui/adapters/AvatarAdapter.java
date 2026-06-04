package com.scholarlyapps.pathlingo.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.scholarlyapps.pathlingo.data.remote.dto.AvatarDto;
import com.scholarlyapps.pathlingo.databinding.ItemAvatarGridBinding;

import java.util.List;

import coil.Coil;
import coil.request.ImageRequest;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    public interface OnAvatarClick {
        void onClick(AvatarDto avatar);
    }

    private List<AvatarDto> avatars;
    private final OnAvatarClick listener;
    private long selectedId = -1;

    public AvatarAdapter(List<AvatarDto> avatars, OnAvatarClick listener) {
        this.avatars = avatars;
        this.listener = listener;
    }

    public void setData(List<AvatarDto> data) {
        this.avatars = data;
        notifyDataSetChanged();
    }

    public void setSelected(long id) {
        selectedId = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAvatarGridBinding binding = ItemAvatarGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(avatars.get(position), listener, selectedId);
    }

    @Override
    public int getItemCount() {
        return avatars != null ? avatars.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final ItemAvatarGridBinding binding;

        ViewHolder(ItemAvatarGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(AvatarDto avatar, OnAvatarClick listener, long selectedId) {
            binding.txtAvatarName.setText(avatar.name);

            boolean isSelected = avatar.id == selectedId;
            binding.cardAvatar.setStrokeWidth(isSelected || avatar.isActive ? 3 : 0);
            binding.cardTick.setVisibility(isSelected ? View.VISIBLE : View.GONE);

            if (avatar.isOwned) {
                binding.txtAvatarPrice.setText(avatar.isActive ? "Active" : "Select");
                binding.txtAvatarPrice.setBackgroundResource(com.scholarlyapps.pathlingo.R.drawable.bg_chip_sage);
            } else if (avatar.coinPrice == 0) {
                binding.txtAvatarPrice.setText("FREE");
                binding.txtAvatarPrice.setBackgroundResource(com.scholarlyapps.pathlingo.R.drawable.bg_chip_sage);
            } else {
                binding.txtAvatarPrice.setText(avatar.coinPrice + " coins");
                binding.txtAvatarPrice.setBackgroundResource(com.scholarlyapps.pathlingo.R.drawable.bg_pill_blue);
            }

            if (avatar.imageUrl != null && !avatar.imageUrl.isEmpty()) {
                ImageViewCompat.setImageTintList(binding.imgAvatar, null);
                Coil.imageLoader(binding.imgAvatar.getContext()).enqueue(
                        new ImageRequest.Builder(binding.imgAvatar.getContext())
                                .data(avatar.imageUrl)
                                .target(binding.imgAvatar)
                                .build()
                );
            }

            binding.getRoot().setOnClickListener(v -> listener.onClick(avatar));
        }
    }
}
