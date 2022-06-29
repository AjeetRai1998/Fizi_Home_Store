package digi.coders.capsicostorepartner.helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import digi.coders.capsicostorepartner.adapter.SimpleMenuLayoutAdapter;
import digi.coders.capsicostorepartner.adapter.SpinnerMenuLayoutAdapter;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final SimpleMenuLayoutAdapter mAdapter;

    public ItemMoveCallback(SimpleMenuLayoutAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }



    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof SimpleMenuLayoutAdapter.MyHolder) {
                SimpleMenuLayoutAdapter.MyHolder myViewHolder=
                        (SimpleMenuLayoutAdapter.MyHolder) viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }

        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        mAdapter.onMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof SimpleMenuLayoutAdapter.MyHolder) {
            SimpleMenuLayoutAdapter.MyHolder myViewHolder=
                    (SimpleMenuLayoutAdapter.MyHolder) viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(SimpleMenuLayoutAdapter.MyHolder myViewHolder);
        void onRowClear(SimpleMenuLayoutAdapter.MyHolder myViewHolder);
        void onMoved(int fromPosition, int toPosition);

    }

}