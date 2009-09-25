using System;
using System.Collections.Generic;
using System.Text;
using System.Globalization;

namespace Com.BkitMobile.Poma.Mobile.Api.Geometry
{
    /// <summary>
    /// Contains coordinate of locaton
    /// </summary>
    public struct Geocode
    {
        public static readonly Geocode Null = new Geocode(double.PositiveInfinity, double.PositiveInfinity);
        double myLatitude;

        /// <summary>
        /// Latitude
        /// </summary>
        public double Latitude
        {
            get { return myLatitude; }
            set { myLatitude = value; }
        }
        double myLongitude;

        /// <summary>
        /// Longitude
        /// </summary>
        public double Longitude
        {
            get { return myLongitude; }
            set { myLongitude = value; }
        }

        public Geocode(double latitude, double longitude)
        {
            myLatitude = latitude;
            myLongitude = longitude;
        }

        public Geocode(string geocodeStr)
        {
            NumberFormatInfo info = new NumberFormatInfo();
            info.PercentDecimalSeparator = ".";
            try
            {
                string[] geocode = geocodeStr.Split(',');
                myLatitude = double.Parse(geocode[0], info);
                myLongitude = double.Parse(geocode[1], info);
            }
            catch
            {
                myLatitude = double.PositiveInfinity;
                myLongitude = double.PositiveInfinity;
            }
        }

        public override string ToString()
        {
            NumberFormatInfo info = new NumberFormatInfo();
            info.PercentDecimalSeparator = ".";
            return string.Format(info, "{0},{1}", myLatitude, myLongitude);
        }

        public static bool operator ==(Geocode left, Geocode right)
        {
            return left.myLongitude == right.myLongitude && left.myLatitude == right.myLatitude;
        }

        public static bool operator !=(Geocode left, Geocode right)
        {
            return left.myLongitude != right.myLongitude || left.myLatitude != right.myLatitude;
        }

        public override bool Equals(object obj)
        {
            if (!(obj is Geocode)) return false;
            Geocode other = (Geocode)obj;
            return this == other;
        }

        public override int GetHashCode()
        {
            return myLatitude.GetHashCode() + myLongitude.GetHashCode();
        }
    }
}
