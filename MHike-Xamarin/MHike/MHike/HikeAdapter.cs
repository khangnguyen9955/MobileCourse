using Android.Content;
using Android.Views;
using Android.Widget;
using System.Collections.Generic;
using MHike.model;

namespace MHike
{
    public class HikeAdapter : BaseAdapter<Hike>
    {
        private List<Hike> hikes;
        private Context context;

        public HikeAdapter(Context context, List<Hike> hikes)
        {
            this.context = context;
            this.hikes = hikes;
        }

        public override Hike this[int position] => hikes[position];

        public override int Count => hikes.Count;

        public override long GetItemId(int position) => position;

        public override View GetView(int position, View convertView, ViewGroup parent)
        {
            View view = convertView;
            if (view == null)
            {
                view = LayoutInflater.FromContext(context).Inflate(Resource.Layout.hike_item, null);
            }

            TextView hikeNameTextView = view.FindViewById<TextView>(Resource.Id.hikeNameTextView);
            TextView hikeLocationTextView = view.FindViewById<TextView>(Resource.Id.hikeLocationTextView);

            hikeNameTextView.Text = hikes[position].Name;
            hikeLocationTextView.Text = hikes[position].Location;


            return view;
        }
    }
}